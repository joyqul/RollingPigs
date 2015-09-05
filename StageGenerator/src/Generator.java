import Json.JSONArray;
import Json.JSONObject;
import java.io.*;

public class Generator{

	public static void main(String[] args) throws Exception{
		PrintWriter out = new PrintWriter("TestStage.txt");
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
		circle1.put("slots", temp2);
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
		circle1.put("slots", temp3);
		result.put(circle3);

		JSONObject slot1 = new JSONObject();
		slot1.put("type", "slot");
		slot1.put("id", 0);
		slot1.put("x", 309);
		slot1.put("y", 564);
		slot1.put("content", 0);
		result.put(slot1);

		JSONObject slot2 = new JSONObject();
		slot2.put("type", "slot");
		slot2.put("id", 1);
		slot2.put("x", 459);
		slot2.put("y", 564);
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
		result.put(node1);

		JSONObject node2 = new JSONObject();
		node2.put("type", "node");
		node2.put("id", 1);
		node2.put("src", 1);
		node2.put("onSId", 1);
		node2.put("color", 0x10);
		result.put(node2);

		JSONObject node3 = new JSONObject();
		node3.put("type", "node");
		node3.put("id", 2);
		node3.put("src", 2);
		node3.put("onSId", 2);
		node3.put("color", 0x1);
		result.put(node3);

		System.out.println(result.toString());
		out.println(result.toString());
		out.flush();
		out.close();
	}

}
