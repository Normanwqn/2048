package Search;

import java.util.PriorityQueue;

import Game.SimpleGameBoard;
import Game.SimpleTile;

public class AStarGraph {
	//public static final int goal = 2048;
	public AStarGraph() {
		
	}
	
	public int int_heuristic(int x) {
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
	
	
	public int heuristic(SimpleGameBoard g) {
		//Summing up heuristic value of the board
		int sum = 0;
		SimpleTile[][] tilesOfBoard = g.board;
		for (SimpleTile[] row: tilesOfBoard) {
			for (SimpleTile element: row) {
				sum += int_heuristic(element.getValue());
			}
		}
		return sum;
	}
	
	public int moveCost() {
		return 1;
	}
	/*public PriorityQueue<AStarNode> getNeighbour(){
		
	}*/
	//Helper Method used to calculate log
	public static int log(int x, int base)
	{
	    return (int) (Math.log(x) / Math.log(base));
	}
}
