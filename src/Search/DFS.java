package Search;


import Game.AI;
import Game.Direction;
import Game.Heuristic;
import Game.Node;
import Game.SimpleGameBoard;
import Game.SimpleTile;

public class DFS extends AI{
	SearchNode currentRootNode;
	static SearchNode goalNode;
	public final static int SEARCHDEPTH = 10; 
	public int[] poll = new int[4];
	public static int GOAL;
	//public int SEARCHDEPTH = 10;
	
	public DFS(SimpleGameBoard g, int goal) {
		super(g);
		currentRootNode = new SearchNode(g);
		GOAL = goal;
		// TODO Auto-generated constructor stub
	}
	
	public DFS(SimpleGameBoard g) {
		super(g);
		currentRootNode = new SearchNode(g);
		//GOAL = goal;
		// TODO Auto-generated constructor stub
	}
	public static int trialHeuristic(SimpleGameBoard b) {
		int sum = 0;
		if ((b.dead) && (b.findMax() != GOAL)) {
			return Integer.MIN_VALUE;
		}
		SimpleTile[][] tilesOfBoard = b.board;
		for (SimpleTile[] row: tilesOfBoard) {
			for (SimpleTile element: row) {
				if (element == null) {
					sum += 0;
				} else {
					sum += (log(element.value, 2)-2)*Math.pow(log(element.value, 2)-1, log(element.value, 2)-1);
				}
				
			}
		}
		return sum;
	}
	public static int log(int x, int base)
	{
	    return (int) (Math.log(x) / Math.log(base));
	}
	@Override
	public String nextMove() {
		for (int i = 0; i <1; i++) {
			SearchNode cloneRoot = new SearchNode(currentBoard);
			buildTree(cloneRoot, 0);
			Direction firstDirection = cloneRoot.maxDir;
			if (firstDirection == Direction.LEFT) {
				poll[0]++;
			} else if (firstDirection == Direction.RIGHT) {
				poll[1]++;
			} else if (firstDirection == Direction.UP) {
				poll[2]++;
			} else if (firstDirection == Direction.DOWN) {
				poll[3]++;
			}
		}
		switch (findMaxIndex(poll)) {
		case 0: return "LEFT";
		case 1: return "RIGHT";
		case 2: return "UP";
		case 3: return "DOWN";
		}
		
		return "";
	}
	public static int findMaxIndex(double[] array) {
		int maxIndex = -1;
		double max = Integer.MIN_VALUE;
		for (int i = 0; i< array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	public static int findMaxIndex(int[] array) {
		int maxIndex = -1;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i< array.length; i++) {
			if (array[i] > max) {
				max = array[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	//Building the tree while evaluating the value of each branch of the tree
	//Propagate backwards to the node level
	public static void buildTree(SearchNode currentNode, int depth) {
		//DFS tree building
		
		
		if ((depth >= SEARCHDEPTH) || (currentNode.NodeGame.dead)) {
			if (currentNode.NodeGame.dead) {
				currentNode.value = -1;
			}
			currentNode.value = Heuristic.snakeShape(currentNode.NodeGame);
		} else 
		{
			
			//Direction maxDir = Direction.NULL;
			double maxPolledVal = Integer.MIN_VALUE;
			double[] maxOpval = {0,0,0,0};
			int pollLength = 1;
			int[] poll = {0,0,0,0};
			for (int ii = 0; ii < pollLength; ii++) {
				double max = Integer.MIN_VALUE;
				int maxOp = Integer.MIN_VALUE;
				int edgeIndex = 0;
				currentNode.adjacencies.clear();
				
				for (int i = 0; i < 4; i++) {
					boolean same = false;
					
					SimpleGameBoard temp = new SimpleGameBoard(currentNode.NodeGame);
					//Iterating through the four directions
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
					//System.out.println(temp.same(currentNode.NodeGame));
					if (temp.same(currentNode.NodeGame)){
						
						same = true;
						//currentNode.adjacencies.add(new SearchEdge(new SearchNode(temp, keyPressed)));
						//System.out.println("Same");
						//currentNode.adjacencies.get(edgeIndex).target.value = -1;
						continue;
					} else  {
						//System.out.println("Depth:" + depth +" Branch:"+edgeIndex);
						currentNode.adjacencies.add(new SearchEdge(new SearchNode(temp, keyPressed)));
						buildTree(currentNode.adjacencies.get(edgeIndex).target, depth+1);
						/*if (max < currentNode.adjacencies.get(edgeIndex).target.value) {
							maxDir = currentNode.adjacencies.get(edgeIndex).target.dir;
							max = currentNode.adjacencies.get(edgeIndex).target.value;
						}*/
						//maxOpval[i] += currentNode.adjacencies.get(edgeIndex).target.value;
						//buildTree(currentNode.adjacencies.get(edgeIndex).target, depth+1);
						edgeIndex ++;	
						
					}
					
				} //end of the inner for-loop
				/*switch (findMaxIndex(maxOpval)) {
				case 0:
					poll[0]++;
				break;
				case 1:
					poll[1]++;
				break;
				case 2:
					poll[2]++;
				break;
				case 3:
					poll[3]++;
				
				}*/
				double maxNodeVal = Integer.MIN_VALUE;
				Direction maxNodeDir = Direction.NULL;
				for (SearchEdge anEdge: currentNode.adjacencies) {
					SearchNode target = anEdge.target;
					double aVal = target.value;
					if (aVal > maxNodeVal) {
						maxNodeDir = target.dir;
						maxNodeVal = aVal;
					}
				}
				if (maxNodeDir == Direction.LEFT) {
					poll[0]++;
					maxOpval[0] += maxNodeVal;
				} else if (maxNodeDir == Direction.RIGHT) {
					poll[1]++;
					maxOpval[1] += maxNodeVal;
				} else if (maxNodeDir == Direction.UP) {
					poll[2]++;
					maxOpval[2] += maxNodeVal;
				} else if (maxNodeDir == Direction.DOWN) {
					poll[3]++;
					maxOpval[3] += maxNodeVal;
				}
				//System.out.println(maxNodeVal);
			}//end of outer for-loop
			Direction polledDir;
			switch (findMaxIndex(poll)) {
			case 0:
				//System.out.println("LEFT: " + poll[0]);
				currentNode.value = maxOpval[0] /pollLength;
				currentNode.maxDir = Direction.LEFT;
			break;
			case 1:
				//System.out.println("RIGHT: " + poll[1]);
				currentNode.value = maxOpval[1] /pollLength;
				currentNode.maxDir = Direction.RIGHT;
			break;
			case 2:
				//System.out.println("UP: " + poll[2]);
				currentNode.value = maxOpval[2] /pollLength;
				currentNode.maxDir = Direction.UP;
			break;
			case 3:
				//System.out.println("DOWN: " + poll[2]);
				currentNode.value = maxOpval[3] /pollLength;
				currentNode.maxDir = Direction.DOWN;
			break;
			
			}
			//Updating the currentNode
			/*currentNode.value = max;
			currentNode.dir = polledDir;
			currentNode.adjacencies.clear();*/
		
		}
		
	}
}
