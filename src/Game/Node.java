package Game;

import java.util.ArrayList;
import java.util.List;

//Based on https://github.com/jeremypchen/uniform-cost-search/blob/master/src/uniformCostSearch/Node.java
public class Node {
	public int depth;
	public int cost;
	public int totalCost;
	public Node parent;
	public SimpleGameBoard NodeGame;

	public List<Node> Children = new ArrayList<Node>();
	
	public Node(Node Parent, int cost, SimpleGameBoard g) {
		this.parent = Parent;
		this.cost = cost;
		this.totalCost = Parent.totalCost + cost;
		this.depth = Parent.depth + 1;
		this.NodeGame = new SimpleGameBoard(g);
	}
	public Node() {
		
	}
	public Node(SimpleGameBoard g) {
		this.NodeGame = new SimpleGameBoard(g);
	}
	public void addChild(Node child){
		Children.add(child);
	}
	
	
}
