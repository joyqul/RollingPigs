import java.io.*;
import java.util.*;

public class Calculator{

	ArrayList<Circle> circleList;
	ArrayList<Slot> slotList;
	ArrayList<Node> nodeList;
	PrintWriter out;

	public Calculator(ArrayList<Circle> circleList, ArrayList<Slot> slotList, ArrayList<Node> nodeList, String name) throws Exception{
		this.circleList = circleList;
		this.slotList = slotList;
		this.nodeList = nodeList;
		Collections.sort(nodeList, new NodeCmp());
		out = new PrintWriter(name+"sol.txt");
	}

	int calculate(){
		Map<State, State> map = new HashMap<>();
		Map<State, Integer> op = new HashMap<>();
		Queue<State> queue = new ArrayDeque<>();
		State original = new State();
		queue.add(original);
		map.put(original, null);
		while(!queue.isEmpty()){
			State now = queue.poll();
			//System.out.println("now = "+now+", step:"+now.step);
			if(now.checkWin()){
				trace(now, map, op);
				out.flush();
				out.close();
				return now.step;
			}
			for(int i=0;i<circleList.size();i++){
				State next = now.rotate(i);
				if(!map.containsKey(next)){
					map.put(next, now);
					queue.add(next);
					op.put(next, i);
				}
			}
		}
		return -1;
	}

	void trace(State now, Map<State, State> map, Map<State, Integer> op){
		if(now == null) return;
		trace(map.get(now), map, op);
		if(op.get(now) != null) out.println(op.get(now));
	}

	class State implements Comparable<State>{

		char[] assignment;
		String string;
		int step;

		State(){
			assignment = new char[slotList.size()];
			for(Slot s : slotList) assignment[s.id] = ((char)(nodeList.get(s.content).color+'0'));
			string = new String(assignment);
			step = 0;
		}

		State(char[] assignment, int step){
			this.assignment = assignment;
			string = new String(assignment);
			this.step = step;
		}

		State rotate(int n){
			Circle c = circleList.get(n);
			char[] assignment = Arrays.copyOf(this.assignment, this.assignment.length);
			char last = 0, next;
			for(Slot s : c.slots){
				next = assignment[s.id];
				assignment[s.id] = last;
				last = next;
			}
			assignment[c.slots.get(0).id] = last;
			return new State(assignment, step+1);
		}

		boolean checkWin(){
			boolean[] legal = new boolean[nodeList.size()];
			Arrays.fill(legal, false);
			for(Circle c : circleList){
				for(Slot s : c.slots){
					int color = assignment[s.id]-'0';
					legal[s.id] = legal[s.id] || ((color&c.color) != 0);
				}
			}
			for(int i=0;i<legal.length;i++) if(!legal[i]) return false;
			return true;
		}

		@Override
		public String toString(){
			return string;
		}

		@Override
		public int compareTo(State rhs){
			return string.compareTo(rhs.string);
		}

		@Override
		public boolean equals(Object obj){
			if(obj == null) return false;
			State rhs = (State)obj;
			return string.equals(rhs.string);
		}

		@Override
		public int hashCode(){
			return string.hashCode();
		}
	}

	class NodeCmp implements Comparator<Node>{

		@Override
		public int compare(Node lhs, Node rhs){
			return Integer.compare(lhs.id, rhs.id);
		}

	}
}

