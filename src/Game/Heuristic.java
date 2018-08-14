package Game;

public class Heuristic {
	private Heuristic() {
		
	}
	public static double snakeShape(SimpleGameBoard g) {
		double sum = 0;
		double[][] weights = {{Math.pow(4,15), Math.pow(4,14), Math.pow(4,13),Math.pow(4,12)}, 
				{Math.pow(4,8), Math.pow(4,9), Math.pow(4,10),Math.pow(4,11)},
				{Math.pow(4,7), Math.pow(4,6), Math.pow(4,5),Math.pow(4,4)},
				{Math.pow(4,0), Math.pow(4,1), Math.pow(4,2),Math.pow(4,3)}};
		/*double[][] weights = {{16,15,14,13}, 
				{9,10,11,12},
				{8,7,6,5},
				{1,2,3,4}};*/
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				if (g.board[i][j] != null) {
					sum += weights[i][j]* (Math.log(g.board[i][j].getValue())/ Math.log(2));
				}
			}
		}
		return sum;
	}
	// measures how smooth the grid is (as if the values of the pieces
	// were interpreted as elevations). Sums of the pairwise difference
	// between neighboring tiles (in log space, so it represents the
	// number of merges that need to happen before they can merge). 
	// Note that the pieces can be distant
	public static double smoothness(SimpleGameBoard g) {
		double smoothness = 0;
		for (int x = 0; x <4; x++) {
			for (int y = 0; y < 4; y++) {
				if (g.cellOccupied(new Point(x,y))) {
					int value = (int) ((int) Math.log(g.board[x][y].getValue()) / Math.log(2));
					for (int direction = 1; direction <= 2; direction++) {
						vector v = new vector(direction);
						Point targetCell = g.findFarthestPosition(new Point(x,y), v);
						//g.printBoard();
						//System.out.println("Target "+targetCell.getRow()+" "+targetCell.getCol());
							if (g.cellOccupied(targetCell)){
							int targetValue = (int) (Math.log(g.board[targetCell.row][targetCell.col].getValue())/ Math.log(2));
							smoothness -= Math.abs(value - targetValue);
						}
					}
				}
			}
		}
		//:System.out.println("Smoothness"+smoothness);
		return smoothness;
	}
	// counts the number of isolated groups. 
	public static double islands(SimpleGameBoard g) {
		
		int islands = 0;
		for (int x = 0; x<4; x++) {
			for (int y = 0; y < 4; y++) {
				if (g.board[x][y] != null) {
					g.board[x][y].marked = false;
				}
			}
		}
		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				if ((g.board[x][y] != null) && (!g.board[x][y].marked)) {
					islands++;
					mark(x,y,g.board[x][y].value, g);
				}
			}
		}
		return islands;
	}
	//Marking different islands of tiles
	public static void mark(int x, int y, int value, SimpleGameBoard g) {
		if ((x>=0) && (x<=3) && (y>=0) && (y<=3) && (g.board[x][y] != null) && (g.board[x][y].getValue() == value) && (!g.board[x][y].marked)) {
			if (g.board[x][y] != null) {
				g.board[x][y].marked = true;
				for (int i = 0; i <= 3; i++) {
					vector v = new vector(i);
					mark(x + v.x, y+ v.y, value, g);
				}
			}
		}
	}
	// measures how monotonic the grid is. This means the values of the tiles are strictly increasing
	// or decreasing in both the left/right and up/down directions
	public static double monotonicity2(SimpleGameBoard g) {
		int[] totals = {0,0,0,0};
		//up/down direction? left/right
		for (int x = 0; x<4;x++) {
			int current = 0;
			int next = current +1;
			while (next < 4) {
				while ((next < 4) && (!g.cellOccupied(new Point(x, next)))) {
					next++;
				}
				if (next >=4) next--;
				int currentValue = (int) (g.cellOccupied(new Point(x, current))?
						Math.log(g.board[x][current].getValue()) / Math.log(2):
							0);
				int nextValue = (int) (g.cellOccupied(new Point(x, next))?
						Math.log(g.board[x][next].getValue()) / Math.log(2):
							0);
				if (currentValue > nextValue) {
					totals[0] += (nextValue - currentValue);
				} else if (currentValue < nextValue) {
					totals[1] += (currentValue - nextValue);
				}
				current = next;
				next++;
			}
			
		}
		for (int y = 0; y<4;y++) {
			int current = 0;
			int next = current +1;
			while (next < 4) {
				while ((next < 4) && (!g.cellOccupied(new Point(next,y)))) {
					next++;
				}
				if (next >=4) next--;
				int currentValue = (int) (g.cellOccupied(new Point(current,y))?
						Math.log(g.board[current][y].getValue()) / Math.log(2):
							0);
				int nextValue = (int) (g.cellOccupied(new Point(next,y))?
						Math.log(g.board[next][y].getValue()) / Math.log(2):
							0);
				if (currentValue > nextValue) {
					totals[2] += (nextValue - currentValue);
				} else if (currentValue < nextValue) {
					totals[3] += (currentValue - nextValue);
				}
				current = next;
				next++;
			}
			
		}
		int a = Math.max(totals[0], totals[1])+Math.max(totals[2], totals[3]);
		//System.out.println("Monotonicity: " + a);
		return a;
	}
	
}

