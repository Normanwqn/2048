package Search;
import java.util.ArrayList;
import java.util.PriorityQueue;

import Game.Direction;
import Game.Node;
import Game.SimpleGameBoard;

public class SearchNode extends Node {
	
	public double value;
	public double exmmScore;
    public int g_scores;
    public int h_scores;
    public int f_scores = 0;
    public Direction dir;
    public Direction maxDir;
    
    public ArrayList<SearchEdge> adjacencies;
    
    public static int SEARCHDEPTH = 10;
	PriorityQueue<SearchNode> AStarNodeChildren = new PriorityQueue<SearchNode>(new NodeComparator());
	public SearchNode(SearchNode n, double exmmScore) {
		this.exmmScore = exmmScore;
		this.value = n.value;
		this.g_scores =n.g_scores;
		this.h_scores = n.h_scores;
		this.f_scores = n.f_scores;
		this.dir = n.dir;
		this.maxDir = n.maxDir;
		adjacencies = new ArrayList<SearchEdge>();
		
	}
	public SearchNode(double exmmScore) {
		this.exmmScore = exmmScore;
	}
	public SearchNode(SimpleGameBoard g) {
		super(g);
		//System.out.println("Is g null in AStarNode?" +(g == null));
		value = g.findMax();
		adjacencies = new ArrayList<SearchEdge>();
	}
	public SearchNode(SimpleGameBoard g, Direction dir) {
		super(g);
		value = g.findMax();
		this.dir = dir;
		adjacencies = new ArrayList<SearchEdge>();
	}
	public SearchNode(SearchNode n, double exmmScore, Direction d) {
		super(n.NodeGame);
		this.value = n.value;
		this.g_scores = n.g_scores;
		this.h_scores = n.h_scores;
		this.f_scores = n.f_scores;
		this.exmmScore = n.exmmScore;
		this.dir = d;
		adjacencies = new ArrayList<SearchEdge>(n.adjacencies);
	}
	public SearchNode(SearchNode n) {
		super(n.NodeGame);
		this.value = n.value;
		this.g_scores = n.g_scores;
		this.h_scores = n.h_scores;
		this.f_scores = n.f_scores;
		this.exmmScore = n.exmmScore;
		this.dir = n.dir;
		adjacencies = new ArrayList<SearchEdge>(n.adjacencies);
	}
	
}
