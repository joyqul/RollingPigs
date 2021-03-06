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
	static final int totalStages = 30;

	public static void main(String[] args) throws Exception{
		PrintWriter bestMove = new PrintWriter("bestmove.txt");
		for(int i=1;i<totalStages;i++){
			if(i == 10  || i == 15) continue;
			String name = String.format("lv%02d", i);
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
			System.out.println("\nstage "+i+" : ");
			System.out.println(result.toString());
			bestMove.println(i+" "+new Calculator(circleList, slotList, nodeList, name).calculate()+" moves");
			out.println(result.toString());
			out.flush();
			out.close();
		}
		bestMove.flush();
		bestMove.close();
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
					c.slots.add(s);
				}
			}
			Collections.sort(c.slots, c);
		}
	}

	static void readImage(String fileName) throws Exception{
		BufferedImage image = ImageIO.read(new File(fileName));
		final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final double widthRatio = 768.0 / width;
		final double heightRatio = 1280.0 / height;
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
			//if(argb != 0xffffff) System.out.printf("%d %d : %d %d %d %X%n", x, y, red, green, blue, argb);
			switch(argb){
				case 0x0000ff:
					for(Circle c : circleList){
						if(Math.abs(c.x-x) + Math.abs(c.y-y) < 50){
							tooClose = true;
							break;
						}
					}
					if(!tooClose) circleList.add(new Circle(x, y));
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
					if(!tooClose) slotList.add(new Slot(x, y, argb, nodeList));
					break;
			}
			col++;
			if (col == width) {
				col = 0;
				row++;
			}
		}
	}

}

class Circle implements Comparator<Slot>{

	static int counter = 0;
	final String type = "circle";
	int id, src, color, x, y, radius;
	ArrayList<Slot> slots;

	Circle(){
		id = counter;
		slots = new ArrayList<>();
		counter++;
	}

	Circle(int x, int y){
		this();
		this.x = x;
		this.y = y;
	}

	@Override
	public int compare(Slot a, Slot b){
		double ay = a.y - y, ax = a.x - x;
		double by = b.y - y, bx = b.x - x;
		return Double.compare(Math.atan2(ay, ax), Math.atan2(by, bx));
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
		JSONArray temp = new JSONArray();
		for(Slot s : slots) temp.put(s.id);
		obj.put("slots", temp);
		return obj;
	}

}

class Slot{

	static int counter = 0;
	final String type = "slot";
	int id, x, y, content;

	Slot(){
		id = counter++;
	}

	Slot(int x, int y, int color, ArrayList<Node> nodeList){
		this();
		this.x = x;
		this.y = y;
		Node newNode = new Node(id, color);
		content = newNode.id;
		nodeList.add(newNode);
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

class Node{

	static int counter = 0;
	final int[] mapping = {0xffffff, 0xff0000, 0x00ff00, 0x000000, 0xffff00, 0xfffffff, 0xffffff, 0xffffff, 0xff00ff};
	final String type = "node";
	int id, onSId, color, radius;

	Node(int onSId, int colorCode){
		id = counter++;
		radius = 50;
		this.onSId = onSId;
		for(int i=0;i<9;i++) if(mapping[i] == colorCode) color = i;
	}

	JSONObject toJSONObject(){
		JSONObject obj = new JSONObject();
		obj.put("type", type);
		obj.put("id", id);
		obj.put("onSId", onSId);
		obj.put("color", color);
		obj.put("radius", radius);
		return obj;
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
