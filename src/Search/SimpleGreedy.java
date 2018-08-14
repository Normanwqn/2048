package Search;

import java.util.ArrayList;

import Game.AI;
import Game.Direction;
import Game.Heuristic;
import Game.SimpleGameBoard;
import Game.SimpleTile;

public class SimpleGreedy extends AI{

	public SimpleGreedy(SimpleGameBoard g) {
		super(g);
		// TODO Auto-generated constructor stub
	}
	
	public static int trialHeuristic(SimpleGameBoard b) {
		int sum = 0;
		if ((b.dead) && (b.findMax() != 2048)) {
			return Integer.MIN_VALUE;
		}
		SimpleTile[][] tilesOfBoard = b.board;
		for (SimpleTile[] row: tilesOfBoard) {
			for (SimpleTile element: row) {
				if (element == null) {
					sum += 0;
				} else {
					sum += Math.pow(log(element.value, 2), log(element.value, 2));
				}
				
			}
		}
		return sum;
	}
	
	@Override
	public String nextMove() {
		ArrayList<Integer> scores = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++) {
			SimpleGameBoard temp = new SimpleGameBoard(currentBoard);
			boolean same = false;
			switch (i) {
			case 0: temp.moveTiles(Direction.LEFT);
				break;
			case 1: temp.moveTiles(Direction.RIGHT);
				break;
			case 2: temp.moveTiles(Direction.UP);
				break;
			case 3: temp.moveTiles(Direction.DOWN);
				break;
			}
			if (temp.same(currentBoard)) {
				same = true;
			}
			if (same) {
				scores.add(0);
			} else {
				scores.add((int) Heuristic.snakeShape(temp));
			}
		}
		int maxIndex = -1;
		int max = -1;
		for (int i = 0; i < scores.size(); i++) {
			if (scores.get(i) > max) {
				max = scores.get(i);
				maxIndex = i;
			}
		}
		System.out.println("Max: " + max +" Max Index: "+maxIndex);
		String response ="";
		switch (maxIndex) {
		case 0: response= "LEFT";
			break;
		case 1: response= "RIGHT";
			break;
		case 2: response= "UP";
			break;
		case 3: response= "DOWN";
			break;
		}
		return response;
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
	
	
	public static int gameHeuristic(SimpleGameBoard g, boolean b) {
		//Summing up heuristic value of the board
		if (b) {
			return Integer.MIN_VALUE;
		}
		int sum = 0;
		
		if ((g.dead) && (g.findMax() != 2048)) {
			return Integer.MIN_VALUE;
		}
		
		SimpleTile[][] tilesOfBoard = g.board;
		for (SimpleTile[] row: tilesOfBoard) {
			for (SimpleTile element: row) {
				if (element == null) {
					sum += 0 ;
				} else {
					sum += int_heuristic(element.getValue());
				}
			}
		}
		System.out.println(sum);
		return sum;
	}
	
	public static int log(int x, int base)
	{
	    return (int) (Math.log(x) / Math.log(base));
	}
}
