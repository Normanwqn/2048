package Search;
import java.util.ArrayList;


import Game.AI;
import Game.Direction;
import Game.GameBoard;
import Game.Heuristic;
import Game.Point;
import Game.SimpleGameBoard;
import Game.SimpleTile;

public class Expectiminimax extends AI {
	public static long start;
	SearchNode currentRootNode;
	public double bestScore;
	Direction bestMove = Direction.NULL;
	//public static int bestScore = 0;
	public static double newBestScore =0;
	public static final int delay = 8;
	//public static int depth = 0;
	public Expectiminimax(SimpleGameBoard g) {
		super(g);
		// TODO Auto-generated constructor stub
		currentRootNode = new SearchNode(g);
	}


	@Override
	public String nextMove() {
		// TODO Auto-generated method stub
		
		switch (iterativeDeep()) {
		case LEFT: return "LEFT";
		case RIGHT: return "RIGHT";
		case UP: return "UP";
		case DOWN: return "DOWN";
		case NULL: return "Dead";
		}
		return null;
	}
	public Direction iterativeDeep() {
		start = System.currentTimeMillis();
		SearchNode cloneRoot = new SearchNode(currentBoard);
		int depth = 1;
		do {
			System.out.println("D:" + depth + "  "+ GameBoard.formatTime(System.currentTimeMillis()));
			newBestScore =expectiminimax(cloneRoot, depth, "Player", null);
			//System.out.println("D" + depth);
			Direction newBestMove = cloneRoot.maxDir;
			if ((newBestMove == Direction.NULL)) {
				break;
			} else {
				bestMove = newBestMove;
				bestScore = newBestScore;
			}
			//System.out.println("Current Best Move:"+ bestMove);
			if (depth< 100) {depth ++;} else {return Direction.NULL;}
		} while (System.currentTimeMillis() - start < delay/*depth < 2*/);
		return bestMove;
	}
	public double expectiminimax(SearchNode node, int depth, String player, Point choice) {
		//int bestMove = -1;
		//System.out.println("Depth: " + depth +"  Node Value" + node.exmmScore +  " Player:" +player);
		double alpha = 0;
		if ((depth == 0) || (node.NodeGame.won) || (node.NodeGame.dead)) {
			node.exmmScore = eval(node);
			return eval(node);
		}
		if (player.equals("Player")) { //Max Player
			// Return value of maximum-valued child node
			alpha = Integer.MIN_VALUE;
			double[] score = new double[4];
			//for each child of node
			int edgeIndex = 0;
			for (int i = 0; i <=3; i++) {
				SearchNode cloneNode = new SearchNode(node);
				Direction dir = Direction.NULL;
				switch (i) {
				case 0: 
					dir = Direction.UP;
				break;
				case 1: 
					dir = Direction.RIGHT;
				break;
				case 2: 
					dir = Direction.DOWN;
				break;
				case 3: 
					dir = Direction.LEFT;
				break;
				}
				//ArrayList<SearchNode> possibleNode = new ArrayList<SearchNode>();
				cloneNode.NodeGame.moveTiles(dir);
				cloneNode.dir = dir;
				
				
				if (node.NodeGame.same(cloneNode.NodeGame)) {
					//System.out.println("Same Board?"+ node.NodeGame.same(cloneNode.NodeGame));
					/*System.out.println("Orginal Board");
					node.NodeGame.printBoard();
					System.out.println("Moved Board");
					cloneNode.NodeGame.printBoard();*/
					continue;
				}
				
				//Adding the child to the tree
				node.adjacencies.add(new SearchEdge(new SearchNode(cloneNode.NodeGame, dir)));
				score[i] = expectiminimax(node.adjacencies.get(edgeIndex).target, depth, "Computer", null);
				edgeIndex ++;
				
				//node.adjacencies.clear();
			}
			//node.adjacencies.clear();
			double maxScore = Integer.MIN_VALUE;
			Direction maxMove = Direction.NULL;
			//System.out.println(score[0]+" "+score[1]+" "+score[2]+" "+score[3]);
			for (int j = 0; j < 4; j++) {
				if (maxScore < score[j]) {
					maxScore = score[j];
					switch (j) {
					case 0: 
						maxMove = Direction.UP;
					break;
					case 1: 
						maxMove = Direction.RIGHT;
					break;
					case 2: 
						maxMove = Direction.DOWN;
					break;
					case 3: 
						maxMove = Direction.LEFT;
					break;
					}
				}
			}
			alpha = maxScore;
			
			node.maxDir = maxMove; 
			node.exmmScore = maxScore;
			//node.adjacencies.clear();
		} else if (player.equals("Computer")) {
			alpha = Integer.MAX_VALUE;
			//double[] score = new double[4];
			double minScore = Double.MAX_VALUE;
			Direction minMove = Direction.NULL;
			ArrayList<Point> available = node.NodeGame.availableCells();
			ArrayList<Double> scores = new ArrayList<Double>();
			//ArrayList<Direction> directions = new ArrayList<Direction>();
			int scoreIndex = 0;
			for (Point pos: available) {
				//SearchNode cloneNode = new SearchNode(node);
				scores.add(expectiminimax(node, depth, "Chance", pos));
				scoreIndex ++;
			}
			for (int j = 0; j < scoreIndex; j++) {
				if (minScore > scores.get(j)) {
					minScore = scores.get(j);
					minMove = node.adjacencies.get(j).target.dir;
				}
			}
			alpha = minScore; 
		} else if (player.equals("Chance")) {
			alpha = 0;
			int edgeIndex = 0;
			double[] p = {0.8,0.2}; //Iterating through the two possibilities
			double scoreSum = 0;
			for (int i = 0; i <= 1; i++) {
				int value = (i == 0)? 2: (i == 1)? 4:0;
				SearchNode cloneNode = new SearchNode(node);
				SimpleTile tile = new SimpleTile(value, choice.row, choice.col);
				cloneNode.NodeGame.insertTile(tile);
				node.adjacencies.add(new SearchEdge(new SearchNode(cloneNode.NodeGame)));
				scoreSum += p[i]*expectiminimax(node.adjacencies.get(edgeIndex).target, depth-1, "Player", null);
				edgeIndex ++; 
			}
			alpha = scoreSum;
			node.exmmScore = scoreSum;
			//node.adjacencies.clear();
		}
		return alpha;
	}
	public double eval(SearchNode thisNode) {
		return Heuristic.snakeShape(thisNode.NodeGame);
	}
}
