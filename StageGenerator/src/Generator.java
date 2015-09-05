import Json.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.*;

public class Generator{

	static ArrayList<Circle> circleList;
	static ArrayList<Slot> slotList;
	static ArrayList<Node> nodeList;
	static String[] names = {"lv01", "lv02", "lv03", "lv04", "lv05", "lv06", "lv07"};

	public static void main(String[] args) throws Exception{
		for(String name : names){
			Circle.counter = Slot.counter = Node.counter = 0;
			PrintWriter out = new PrintWriter(name+"jsom.txt");
			circleList = new ArrayList<>();
			slotList = new ArrayList<>();
			nodeList = new ArrayList<>();
			readImage(name+".jpg");
			readFile(name+".txt");
			JSONArray result = new JSONArray();
			for(Circle c : circleList) result.put(c.toJSONObject());
			for(Slot s : slotList) result.put(s.toJSONObject());
			for(Node n : nodeList) result.put(n.toJSONObject());
			System.out.println(result.toString());
			out.println(result.toString());
			out.flush();
			out.close();
		}
	}

	static void readFile(String fileName) throws Exception{
		Scan scan = new Scan(fileName);
		for(Circle c : circleList){
			int radius = scan.nextInt();
			int src = scan.nextInt();
			c.radius = radius;
			c.color = 1 << src;
			c.src = src;
			for(Slot s : slotList){
				double dist = Math.sqrt((c.x-s.x)*(c.x-s.x) + (c.y-s.y)*(c.y-s.y));
				if(Math.abs(dist - radius) < 50){
					c.slots.put(s.id);
				}
			}
		}
	}

	static void readImage(String fileName) throws Exception{
		BufferedImage image = ImageIO.read(new File(fileName));
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		System.out.println("read image : "+width+" * "+height);
		final double widthRatio = 768.0 / width;
		final double heightRatio = 1280.0 / height;
		final boolean hasAlphaChannel = image.getAlphaRaster() != null;
		if(hasAlphaChannel) throw new InputMismatchException("ALPHA!!!!!");
		final int pixelLength = 3;
		for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
			int argb = 0;
			int blue = ((int) pixels[pixel] & 0xff); // blue
			int green = (((int) pixels[pixel + 1] & 0xff)); // green
			int red = (((int) pixels[pixel + 2] & 0xff)); // red
			int x = ((int)(col*widthRatio));
			int y = ((int)(row*heightRatio));
			boolean tooClose = false;
			if(blue > 225) blue = 0xff;
			else blue = 0;
			if(green > 225) green = 0xff;
			else green = 0;
			if(red > 225) red = 0xff;
			else red = 0;
			argb += (blue + (green << 8) + (red << 16));
			if(argb != 0xffffff) System.out.printf("%d %d : %d %d %d %X%n", x, y, red, green, blue, argb);
			switch(argb){
				case 0x0000ff:
					for(Circle c : circleList){
						if(Math.abs(c.x-x) + Math.abs(c.y-y) < 50){
							tooClose = true;
							break;
						}
					}
					if(!tooClose) new Circle(x, y);
					break;
				case 0xff0000:
				case 0x00ff00:
				case 0xffff00:
				case 0xff00ff:
				case 0x000000:
					for(Slot s : slotList){
						if(Math.abs(s.x-x) + Math.abs(s.y-y) < 50){
							tooClose = true;
							break;
						}
					}
					if(!tooClose) new Slot(x, y, argb);
					break;
			}
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}
	}

	static class Circle{

		static int counter = 0;
		final String type = "circle";
		int id, src, color, x, y, radius;
		JSONArray slots;

		Circle(){
			id = counter;
			slots = new JSONArray();
			counter++;
			circleList.add(this);
		}

		Circle(int x, int y){
			this();
			this.x = x;
			this.y = y;
		}

		JSONObject toJSONObject(){
			JSONObject obj = new JSONObject();
			obj.put("type", type);
			obj.put("id", id);
			obj.put("src", src);
			obj.put("color", color);
			obj.put("x", x);
			obj.put("y", y);
			obj.put("radius", radius);
			obj.put("slots", slots);
			return obj;
		}

	}

	static class Slot{

		static int counter = 0;
		final String type = "slot";
		int id, x, y, content;

		Slot(){
			id = counter++;
			slotList.add(this);
		}

		Slot(int x, int y, int color){
			this();
			this.x = x;
			this.y = y;
			Node newNode = new Node(id, color);
			content = newNode.id;
		}

		JSONObject toJSONObject(){
			JSONObject obj = new JSONObject();
			obj.put("type", type);
			obj.put("id", id);
			obj.put("x", x);
			obj.put("y", y);
			obj.put("content", content);
			return obj;
		}
	}

	static class Node{

		static int counter = 0;
		final int[] mapping = {0xff0000, 0x00ff00, 0xffff00, 0xff00ff, 0x000000};
		final String type = "node";
		int id, src, onSId, color, radius;

		Node(int onSId, int colorCode){
			id = counter++;
			radius = 50;
			this.onSId = onSId;
			for(int i=0;i<5;i++) if(mapping[i] == colorCode){
				src = i;
				color = (1<<i);
			}
			nodeList.add(this);
		}

		JSONObject toJSONObject(){
			JSONObject obj = new JSONObject();
			obj.put("type", type);
			obj.put("id", id);
			obj.put("src", src);
			obj.put("onSId", onSId);
			obj.put("color", color);
			obj.put("radius", radius);
			return obj;
		}
	}

	static JSONArray testStage(){
		JSONArray result = new JSONArray();

		JSONObject circle1 = new JSONObject();
		circle1.put("type", "circle");
		circle1.put("id", 0);
		circle1.put("src", 0);
		circle1.put("color", 0x1);
		circle1.put("x", 384);
		circle1.put("y", 384);
		circle1.put("radius", 150);
		JSONArray temp1 = new JSONArray();
		temp1.put(0);
		circle1.put("slots", temp1);
		result.put(circle1);

		JSONObject circle2 = new JSONObject();
		circle2.put("type", "circle");
		circle2.put("id", 1);
		circle2.put("src", 0);
		circle2.put("color", 0x10);
		circle2.put("x", 234);
		circle2.put("y", 644);
		circle2.put("radius", 150);
		JSONArray temp2 = new JSONArray();
		temp2.put(1);
		circle2.put("slots", temp2);
		result.put(circle2);

		JSONObject circle3 = new JSONObject();
		circle3.put("type", "circle");
		circle3.put("id", 2);
		circle3.put("src", 0);
		circle3.put("color", 0x100);
		circle3.put("x", 534);
		circle3.put("y", 644);
		circle3.put("radius", 150);
		JSONArray temp3 = new JSONArray();
		temp3.put(2);
		circle3.put("slots", temp3);
		result.put(circle3);

		JSONObject slot1 = new JSONObject();
		slot1.put("type", "slot");
		slot1.put("id", 0);
		slot1.put("x", 309);
		slot1.put("y", 514);
		slot1.put("content", 0);
		result.put(slot1);

		JSONObject slot2 = new JSONObject();
		slot2.put("type", "slot");
		slot2.put("id", 1);
		slot2.put("x", 459);
		slot2.put("y", 514);
		slot2.put("content", 1);
		result.put(slot2);

		JSONObject slot3 = new JSONObject();
		slot3.put("type", "slot");
		slot3.put("id", 2);
		slot3.put("x", 384);
		slot3.put("y", 644);
		slot3.put("content", 2);
		result.put(slot3);

		JSONObject node1 = new JSONObject();
		node1.put("type", "node");
		node1.put("id", 0);
		node1.put("src", 0);
		node1.put("onSId", 0);
		node1.put("color", 0x100);
		node1.put("radius", 50);
		result.put(node1);

		JSONObject node2 = new JSONObject();
		node2.put("type", "node");
		node2.put("id", 1);
		node2.put("src", 1);
		node2.put("onSId", 1);
		node2.put("color", 0x10);
		node2.put("radius", 50);
		result.put(node2);

		JSONObject node3 = new JSONObject();
		node3.put("type", "node");
		node3.put("id", 2);
		node3.put("src", 2);
		node3.put("onSId", 2);
		node3.put("color", 0x1);
		node3.put("radius", 50);
		result.put(node3);

		return result;
	}

}

class Scan{

	BufferedReader buffer;
	StringTokenizer tok;

	Scan(String FileName) throws Exception{
		buffer = new BufferedReader(new FileReader(FileName));
	}

	boolean hasNext(){
		while(tok==null || !tok.hasMoreElements()){
			try{
				tok = new StringTokenizer(buffer.readLine());
			}catch(Exception e){
				return false;
			}
		}
		return true;
	}

	String next(){
		if(hasNext()) return tok.nextToken();
		return null;
	}

	int nextInt(){
		return Integer.parseInt(next());
	}

}
