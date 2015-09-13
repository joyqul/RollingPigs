import java.util.*;

public class Calculator{

	ArrayList<Circle> circleList;
	ArrayList<Slot> slotList;
	ArrayList<Node> nodeList;

	public Calculator(ArrayList<Circle> circleList, ArrayList<Slot> slotList, ArrayList<Node> nodeList){
		this.circleList = circleList;
		this.slotList = slotList;
		this.nodeList = nodeList;
		Collections.sort(nodeList, new NodeCmp());
	}

	int calculate(){
		Set<State> set = new HashSet<>();
		Queue<State> queue = new ArrayDeque<>();
		State original = new State();
		queue.add(original);
		set.add(original);
		while(!queue.isEmpty()){
			State now = queue.poll();
			if(now.checkWin()) return now.step;
			for(int i=0;i<circleList.size();i++){
				State next = now.rotate(i);
				if(set.add(next)) queue.add(next);
			}
		}
		return -1;
	}

	class State{

		char[] assignment;
		String string;
		int step;

		State(){
			assignment = new char[slotList.size()];
			int counter = 0;
			for(Slot s : slotList) assignment[counter++] = ((char)nodeList.get(s.content).color);
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
					int color = assignment[s.id];
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
		public boolean equals(Object obj){
			if(obj==null) return false;
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

