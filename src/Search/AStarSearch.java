package Search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import Game.AI;
import Game.Direction;
import Game.SimpleGameBoard;
import Game.SimpleTile;
import Game.Direction;

//A defunct search class which is not working,
//I orginally attempted to use A* search
public class AStarSearch extends AI{
	SearchNode currentRootNode;
	static SearchNode goalNode;
	public final int SEARCHDEPTH = 12; 
	public static int GOAL;
	public AStarSearch(SimpleGameBoard g) {
		super(g);
		currentRootNode = new SearchNode(g);
		GOAL = 64;
		//goalNode = new AStarNode(GOAL);
		// TODO Auto-generated constructor stub
	}
	
	@Override 
	public String nextMove() {
		//Determining the next move
		//Build a 20 level deep game tree
		buildTree(currentRootNode, 0);
		AstarSearch(currentRootNode, GOAL);
		List<SearchNode> path = printPath(goalNode);
		
		Direction firstDirection = path.get(0).dir;
		if (firstDirection == Direction.LEFT) {
			return "LEFT";
		} else if (firstDirection == Direction.RIGHT) {
			return "RIGHT";
		} else if (firstDirection == Direction.UP) {
			return "UP";
		} else if (firstDirection == Direction.DOWN) {
			return "DOWN";
		}
		return "";
	}
	public void buildTree(SearchNode currentNode, int depth) {
		//DFS tree building
		if ((depth >= SEARCHDEPTH) || (currentNode.NodeGame.dead)) {
			return; //Base case exit
		} else {
			int edgeIndex = 0;
			
			for (int i = 0; i < 4; i++) {
				SimpleGameBoard temp = new SimpleGameBoard(currentNode.NodeGame);
				Direction keyPressed =Direction.LEFT;
				if (i == 0) {
					keyPressed = Direction.LEFT;
				} else if (i == 1) {
					keyPressed = Direction.RIGHT;
				} else if (i == 2) {
					keyPressed = Direction.UP;
				} else if (i == 3) {
					keyPressed = Direction.DOWN;
				}
				temp.moveTiles(keyPressed);
				if (! temp.same(currentNode.NodeGame)) {
					System.out.println("Depth:" + depth +" Branch:"+edgeIndex);
					currentNode.adjacencies.add(new SearchEdge(new SearchNode(temp, keyPressed)));
					buildTree(currentNode.adjacencies.get(edgeIndex).target, depth+1);
					edgeIndex ++;
				}
			}
		}
	}
	 public static List<SearchNode> printPath(SearchNode target){
         List<SearchNode> path = new ArrayList<SearchNode>();
         	for(SearchNode node = target; node!=null; node = (SearchNode) node.parent){
         		path.add(node);
         	}
         	Collections.reverse(path);
         	return path; 
	}
	public static void AstarSearch(SearchNode source, int goal){
		Set<SearchNode> explored = new HashSet<SearchNode>();
		PriorityQueue<SearchNode> queue = new PriorityQueue<SearchNode>(new NodeComparator());
		source.g_scores = 0;
		queue.add(source);
		
		boolean found = false;
		 while((!queue.isEmpty())&&(!found)){
			 //the node in having the lowest f_score value
			 SearchNode current = queue.poll();
			 explored.add(current);
			//goal found
             if(current.value == 64){
                     found = true;
                     goalNode = current;
             }
             for (SearchEdge anEdge: current.adjacencies) {
            	 SearchNode child = anEdge.target;
            	 int cost = anEdge.cost; //Setting the cost of pressing a key to "1"
            	 int temp_g_scores = current.g_scores + cost;
            	 int temp_f_scores = temp_g_scores + heuristic(child.NodeGame);
            	 /*if child node has been evaluated and 
                 the newer f_score is higher, skip*/
            	 if((explored.contains(child)) && 
                         (temp_f_scores >= child.f_scores)){
                         continue;
                 }
            	 /*else if child node is not in queue or 
                 newer f_score is lower*/
                 
                 else if((!queue.contains(child)) || 
                         (temp_f_scores < child.f_scores)){

                         child.parent = current; //Critical
                         child.g_scores = temp_g_scores;
                         child.f_scores = temp_f_scores;

                         if(queue.contains(child)){
                                 queue.remove(child);
                         }
                         queue.add(child);
                 } 
             }
		 }
	}
	
	 
	public static int int_heuristic(int x) {
		//Calculating how many times it takes for simple 2s to combine into x in total
		int sum = 0;
		if (x == 2) {
			return 0;
		} 
		for (int i = 0; i <= log(x, 2) -2; i++ ) {
			sum += (int) Math.pow(2, i);
		}
		return sum;
	}
	
	
	public static int heuristic(SimpleGameBoard g) {
		//Summing up heuristic value of the board
		int sum = 0;
		if ((g.dead) && (g.findMax() != GOAL)) {
			return Integer.MIN_VALUE;
		}
		SimpleTile[][] tilesOfBoard = g.board;
		for (SimpleTile[] row: tilesOfBoard) {
			for (SimpleTile element: row) {
				if (element == null) {
					sum += 0;
				} else {
					sum += int_heuristic(element.getValue());
				}
				
			}
		}
		return sum;
	}
	
	public static int log(int x, int base)
	{
	    return (int) (Math.log(x) / Math.log(base));
	}
}
