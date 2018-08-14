package Search;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import Game.AI;
import Game.Direction;
import Game.GameBoard;
import Game.Heuristic;
import Game.SimpleGameBoard;
import Game.SimpleTile;
import Game.Point;

public class minMax extends AI{
	public static long start;
	public static int bestMove = -1;
	public static int newBestMove;
	public static int bestScore = 0;
	public static int newBestScore =0;
	public static int minSearchTime = 10000000;
	//public static int positions;
	public static int cutoffs;
	
	
	public minMax(SimpleGameBoard g) {
		super(g);
		// TODO Auto-generated constructor stub
	}
	
	public double[] search(int depth, double alpha, double beta, int positions, int cutoffs, boolean playerTurn) {
		double bestScore;
		int bestMove = -1;
		double[] result;
		Direction resultMove;
		double resultScore;
		//System.out.println(depth + " " + alpha + " "+ beta + "  "+ positions +" " +cutoffs + " " + playerTurn);
		//maximising player
		if (playerTurn) {
			bestScore = alpha;
			for (int d = 0; d <= 3; d++) {
				SimpleGameBoard newGrid = new SimpleGameBoard(currentBoard);
				int horizontalDirection = 0;
				int verticalDirection = 0;
				Direction dir = Direction.NULL;
				switch (d) {
				case 0: horizontalDirection = -1;
					dir = Direction.UP;
				break;
				case 1: horizontalDirection = 1;
					dir = Direction.RIGHT;
				break;
				case 2: verticalDirection = -1;
					dir = Direction.DOWN;
				break;
				case 3: verticalDirection = 1;
					dir = Direction.LEFT;
				break;
				}
				newGrid.moveTiles(dir);
				//System.out.println("Move "+ d);
				//System.out.println("Original Grid:");
				//currentBoard.printBoard();
				//System.out.println("New Grid:");
				//newGrid.printBoard();
				
				if (newGrid.same(currentBoard)) {
					//System.out.println(d +" is the same.");
					continue;
				}
				//newGrid.printBoard();
				if (! (newGrid.same(currentBoard))) {
					//System.out.println("Try moving "+d);
					//System.out.println();
					positions ++;
					//newGrid.printBoard();
					if (newGrid.findMax() ==2048) {
						/*minMax.bestMove = dir;
						minMax.bestScore = 1000;
						minMax.positions = positions;
						minMax.cutoffs = cutoffs;*/
						double[] temp = {d, 10000, positions, cutoffs};
						result = temp;
						return result;
					}
					minMax newAI = new minMax(newGrid);
					//System.out.println("Depth" + depth);
					if (depth == 0) {
						//newGrid.printBoard();
						
						//System.out.println("evaluated");
						//System.out.println(eval(newGrid));
						double[] temp = {d, eval(newGrid)};
						//System.out.println("evaluated");
						result = temp;
					} else {
						result = newAI.search(depth-1, bestScore, beta, positions, cutoffs, false);
						if (result[1] > 9900) {
							result[1] --;
						}
						positions = (int) result[2];
						cutoffs = (int) result[3];
					}
					//System.out.println("Score "+ result[1]);
					if (result[1] > bestScore) {
						bestScore = result[1];
						bestMove = d;
					}
					if (bestScore > beta) {
						cutoffs++;
						double[] temp = {bestMove, beta, positions, cutoffs};
						return temp;
					}
				}
			}
		}
		else {
			bestScore = beta;
			// try a 2 and 4 in each cell and measure how annoying it is
		    // with metrics from eval
			ArrayList<SimpleTile> candidates = new ArrayList<SimpleTile>();
			ArrayList<Point> cells = currentBoard.availableCells();
			List<List<Double>> scores = new ArrayList<List<Double>>();
			scores.add(new ArrayList<Double>());
			scores.add(new ArrayList<Double>());
			for (int value = 0; value <= 1; value++) {
				for (Point i: cells) {
					//scores.get(value).add(null);
					Point cell = i;
					int v = 2;
					if (value == 0) {
						v = 2;
					} else if (value == 1) {
						v = 4;
					}
					SimpleTile tile = new SimpleTile(v, cell.row, cell.col);
					SimpleGameBoard cloneBoard = new SimpleGameBoard(currentBoard);
					cloneBoard.insertTile(tile);
					//double te = (-Heuristic.smoothness(cloneBoard) + Heuristic.islands(cloneBoard));
					scores.get(value).add((-Heuristic.smoothness(cloneBoard) + Heuristic.islands(cloneBoard)));
					cloneBoard.removeTile(cell.row, cell.col);
				} 
			}
			//now just pick out the most annoying moves
			double maxScore = Math.max(findMax(scores.get(0)), findMax(scores.get(1)));
			for (int value = 0; value <= 1; value++) {
				for (int i = 0;  i < scores.get(value).size(); i++) {
					if (scores.get(value).get(i) == maxScore) {
						//int v = ((value == 0)? 2: (value == 1)? 4:-1);
						candidates.add(new SimpleTile(((value == 0)? 2: (value == 1)? 4:0), cells.get(i).row, cells.get(i).col));
					}
				}
			}
			
			// search on each candidate
			for (int i = 0; i < candidates.size(); i++) {
				SimpleGameBoard newGrid = new SimpleGameBoard(currentBoard);
				newGrid.insertTile(candidates.get(i));
				positions ++;
				minMax newAI = new minMax(newGrid);
				result = search(depth, alpha, bestScore, positions, cutoffs, true);
				positions = (int) result[2];
				cutoffs = (int) result[3];
				if (result[1] < bestScore) {
					bestScore = result[1];
				}
				if (bestScore < alpha) {
					cutoffs++;
					double[] temp = {(Double) null, alpha, positions, cutoffs};
					return temp;
				}
			}
			
		}
		double[] temp = {bestMove, bestScore,positions, cutoffs};
		//System.out.println("Depth: " + depth +"  Best Move: " + bestMove + "  Best Score: "+bestScore + " Player:" +playerTurn);
		return temp;
	}
	public double findMax(List<Double> list) {
		double max = Integer.MIN_VALUE;
		for (double t: list) {
			if (t>max) {
				max = t;
			}
		}
		return max;
	}
	public int iterativeDeep() {
		start = System.currentTimeMillis();
		int depth = 0;
		double[] results;
		do {
			System.out.println("D:" + depth + "  "+ GameBoard.formatTime(System.currentTimeMillis()));
			results = search(depth, -1000, 1000, 0, 0, true);
			//System.out.println("D" + depth);
			int newBestMove = (int) results[0];
			if (newBestMove == -1) {
				break;
			} else {
				bestMove = newBestMove;
				bestScore = newBestScore;
			}
			
			//System.out.println("Current Best Move:"+ bestMove);
			depth ++; 
		} while (System.currentTimeMillis() - start < 100/*depth < 2*/);
		System.out.println("Moving"+bestMove);
		return bestMove;
	}
	//Evaluation Function
	public static double eval(SimpleGameBoard g) {
		double smoothWeight = 0.1;
		double mono2Weight = 1.0;
		double emptyWeight = 2.7;
		double maxWeight = 1.0;
		int emptyCells = g.availableCells().size();
		double snakeWeight = 1.0;
		
		double t = Heuristic.smoothness(g)*smoothWeight + Heuristic.monotonicity2(g)*mono2Weight +  Math.log(emptyCells)*emptyWeight+g.findMax()*maxWeight;
		//System.out.println("H:"+ t);
		return t;
		//return DFS.trialHeuristic(g);
	}
	@Override
	public String nextMove() {
		// TODO Auto-generated method stub
		switch (iterativeDeep()) {
		case 0:
			return "UP";
		case 1:
			return "RIGHT";
		case 2:
			return "DOWN";
		case 3:
			return "LEFT";
		}
		return null;
	}
}
