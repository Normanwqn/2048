package Game;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SimpleGameBoard {
	public static final int ROWS = 4;
	public static final int COLS = 4;
	public final int startingTiles = 2;
	public SimpleTile[][] board;
	public boolean dead; //end of game
	public boolean won; //Won the game
	public int score = 0;
	public int highScore = 0; //Highest Score
	public boolean hasStarted;
	
	public SimpleGameBoard(){
		board = new SimpleTile[ROWS][COLS];
		/*for (int i = 0; i < ROWS; i ++) {
			for (int j = 0; j < COLS; j++) {
				board[i][j] = new SimpleTile(0, i, j);
			}
		}*/
	}
	
	public SimpleGameBoard(SimpleGameBoard g) {
		board = new SimpleTile[ROWS][COLS];
		//System.out.println("g is null " + (g == null));
		for (int i = 0; i < ROWS; i ++) {
			for (int j = 0; j < COLS; j++) {
				if (g.board[i][j] == null) {
					continue;}
				else {
					this.board[i][j] = new SimpleTile(g.board[i][j]);
				}
			}
		}
		score = g.getScore();
	}
	public SimpleGameBoard(SimpleTile[][] current, int cS) {
		board = new SimpleTile[ROWS][COLS];
		for (int i = 0; i < current.length; i ++) {
			for (int j = 0; j < current.length; j++) {
				if (current[i][j] != null) board[i][j] = new SimpleTile(current[i][j]);
			}
		}
		score = cS;
	}
	public void printBoard() {
		for (SimpleTile[] a: board) {
			for (SimpleTile b: a) {
				if (b != null) {
					System.out.print(b.getValue() + " ");
				} else {
					System.out.print("0 ");
				}
				
			}
			System.out.println();
		}
		System.out.println(dead);
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public boolean withinBounds(Point a) {
		return (((a.getCol() >= 0) && (a.getCol() < COLS)) && ((a.getRow() >= 0) && (a.getRow() < ROWS)));
	}
	public boolean checkOutOfBounds(Direction dir, int row, int col) {
		// TODO Auto-generated method stub
		if (dir == Direction.LEFT) {
			return col<0;
		}
		else if (dir == Direction.RIGHT) {
			return col> COLS-1;
		}
		else if (dir == Direction.UP) {
			return row <0;
		}
		else if (dir == Direction.DOWN) {
			return row > ROWS-1;
		}
		return false;
	}
	public boolean move(int row, int col, int horizontalDirection, int verticalDirection, Direction dir) {
		boolean canMove = false;
		SimpleTile current = board[row][col];
		if (current == null) return false;
		boolean move = true;
		int newCol = col;
		int newRow = row;
		while (move) {
			//Predict the next position
			newCol += horizontalDirection;
			newRow += verticalDirection;
			if (checkOutOfBounds(dir, newRow, newCol)) {
				break; //break if out of bounds 
			}
			if (board[newRow][newCol] == null) { //move the tile if it is null
				board[newRow][newCol] = current;
				board[newRow - verticalDirection][newCol - horizontalDirection] = null;
				board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
				canMove = true;
			}
			else if ((board[newRow][newCol].getValue() == current.getValue()) && (board[newRow][newCol].canCombine())){ //Combine 
				board[newRow][newCol].setCanCombine(false);
				board[newRow][newCol].setValue(board[newRow][newCol].getValue()*2);
				canMove = true;
				board[newRow - verticalDirection][newCol - horizontalDirection] = null;
				board[newRow][newCol].setSlideTo(new Point(newRow, newCol));
				SimpleTile newTile = (SimpleTile) board[newRow][newCol];
				newTile.setCombineAnimation(true);
				//add to score 
				score += board[newRow][newCol].getValue();
			} else {
				move = false;
			}
		}
		return canMove;
	}
	public boolean moveTiles(Direction dir) {
		boolean canMove = false; //Whether the tiles can move or not
		int horizontalDirection = 0;
		int verticalDirection = 0;
		if (dir == Direction.LEFT) {
			horizontalDirection = -1;
			//System.out.println("Moving Left");
			for (int row = 0; row < ROWS; row++) {
				for (int col = 0; col < COLS; col++) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					}else { move(row, col, horizontalDirection, verticalDirection, dir);}
				} 
			}
		}
		//The direction of update needs to be changed. Update from the right
		else if (dir == Direction.RIGHT) {
			horizontalDirection = 1;
			//System.out.println("Moving Right");
			for (int row = 0; row < ROWS; row++) {
				for (int col = COLS-1; col >=0; col--) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					} else move(row, col, horizontalDirection, verticalDirection, dir);
				} 
			}
		}
		else if (dir == Direction.UP) {
			verticalDirection = -1;
			//System.out.println("Moving Up");
			for (int row = 0; row < ROWS; row++) {
				for (int col = 0; col < COLS; col++) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					} else move(row, col, horizontalDirection, verticalDirection, dir);
				} 
			}
		}
		else if (dir == Direction.DOWN) {
			verticalDirection = 1;
			//System.out.println("Moving Down");
			for (int row = ROWS -1; row >= 0; row--) {
				for (int col = 0; col < COLS; col++) {
					if (!canMove) {
						canMove = move(row, col, horizontalDirection, verticalDirection, dir);
					} else move(row, col, horizontalDirection, verticalDirection, dir);
				} 
			}
		} else {
			System.out.println(dir + "is not a valid direction.");
		}
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				SimpleTile current = board[row][col];
				if (current == null) {
					continue;
				}
				current.setCanCombine(true);
			}
		}
		if (!canMove) {
			//spawnRandom(); Override
			//check Dead
			checkDead();
			//return true;
		}
		
		return canMove;
		//printBoard();
	}
	public void checkDead() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				if (board[row][col] == null) {
					//System.out.println("Not dead yet");
					return;
				} //There is still space
				if (checkSurroundingTiles(row,col,board[row][col])) {
					//System.out.println("Not dead yet");
					return; //Check the surrounding Tiles to see whether it is able to combine or not
				}
			}
		}
		//System.out.println("Dead");
		dead = true;
		if (score >= highScore) {
			highScore = score;
		}
		//setHighScore(); Override this 
		//setHighScore; Timer...
	}
	public boolean checkSurroundingTiles(int row, int col, SimpleTile current) {
		if (row >0) {
			SimpleTile check = board[row-1][col]; //Move up a row
			if (check == null) return true;
			if (current.getValue() == check.getValue()) return true;
		}
		if (row < ROWS-1) {
			SimpleTile check = board[row+1][col]; //Moving down a row
			if ((check == null) ||(current.getValue() == check.getValue())) return true;
		}
		if (col > 0) {
			SimpleTile check = board[row][col-1]; //Move to the left
			if (check == null) return true;
			if (current.getValue() == check.getValue()) return true;
		}
		if (col < COLS-1) {
			SimpleTile check = board[row][col+1]; //Move to the right
			if (check == null) return true;
			if (current.getValue() == check.getValue()) return true;
		}
		return false;
	}
	public void checkKeys() {
		//System.out.println("Checking keys");
		if (Keyboard.typed(KeyEvent.VK_LEFT)) {
			//System.out.println("Checked Left");
			moveTiles(Direction.LEFT);
			if (!hasStarted) hasStarted = true; 
		}
		if (Keyboard.typed(KeyEvent.VK_RIGHT)) {
			//System.out.println("CHECKED RIGHT");
			moveTiles(Direction.RIGHT);
			if (!hasStarted) hasStarted = true; 
			
		}
		if (Keyboard.typed(KeyEvent.VK_UP)) {
			//System.out.println("CHECKED UP");
			moveTiles(Direction.UP);
			if (!hasStarted) hasStarted = true; 
			
		}
		if (Keyboard.typed(KeyEvent.VK_DOWN)) {
			//System.out.println("CHECKED DOWN");
			moveTiles(Direction.DOWN);
			if (!hasStarted) hasStarted = true; 	
		}
	}
	public SimpleTile[][] getBoard() {
		return board;
	}
	public void setBoard(Tile[][] board) {
		this.board = board;
	}
	public ArrayList<Point> availableCells() {
		ArrayList<Point> empty = new ArrayList<Point>();
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (board[i][j] == null) {
					empty.add(new Point(i,j));
				}
			}
		}
		return empty;
	}
	public boolean cellOccupied(Point a) {
		int a_x = a.getRow();
		int a_y = a.getCol();
		if (!withinBounds(a)) {
			return false;
		}
		if ((board[a_x][a_y] != null)) {
			if (board[a_x][a_y].getValue() != 0 ) return true; 
			else return false;
		}
		return false;
	}
	public boolean cellAvailable(Point a) {
		return !cellOccupied(a);
	}
	public void insertTile(SimpleTile t) {
		int t_x = t.getSlideTo().getRow();
		int t_y = t.getSlideTo().getCol();
		if (board[t_x][t_y] == null) {
			board[t_x][t_y] = new SimpleTile(t);
		}
	}
	public void removeTile(int row, int col) {
		if (board[row][col] != null) {
			board[row][col] = null;
		}
	}
	public Point findFarthestPosition(Point cell, vector v ) {
		Point previous;
		do {
			previous = new Point(cell);
			cell.row = cell.row + v.x;
			cell.col = cell.col + v.y;
		} while (withinBounds(cell) && cellAvailable(cell));
		return cell; //Used to check if a merge is required Next
	}
	public boolean same(SimpleGameBoard g) {
		int[][] a = new int[4][4];
		int[][] b = new int[4][4];
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j<COLS; j++) {
				if (g.board[i][j] != null) {
					a[i][j] = g.board[i][j].getValue();
				} else {
					a[i][j] = 0;
				}
			}
		}
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j<COLS; j++) {
				if (board[i][j] != null) {
					b[i][j] = board[i][j].getValue();
				} else {
					b[i][j] = 0;
				}
			}
		}
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j<COLS; j++) {
				if (board[i][j] != null) {
					b[i][j] = board[i][j].getValue();
				} else {
					b[i][j] = 0;
				}
			}
		}
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j<COLS;j++) {
				if (a[i][j] != b[i][j]) {
					return false;
				}
			}
		}
		/*
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j<COLS; j++) {
				if ((g.board[i][j] == null) && (board[i][j] == null)) {
					continue;
				}
				if (!(g.board[i][j] == null) && (board[i][j] == null)) {
					return false;
				}
				if ((g.board[i][j] == null) && !(board[i][j] == null)) {
					return false;
				}
				if (g.board[i][j].value != board[i][j].value) {
					return false;
				}
			}
		}*/
		return true;
	}
	public int findMax() {
		SimpleTile[][] b = board;
		
		int max = -1;
		for (SimpleTile[] row: b) {
			for (SimpleTile element: row) {
				//System.out.println("Is element null" + (element == null));
				if ((element != null) && (element.value > max)) {
					max = element.value;
				}
			}
		}
		return max;
	}
	public double findAverage() {
		SimpleTile[][] b = board;
		
		int sum = 0;
		for (SimpleTile[] row: b) {
			for (SimpleTile element: row) {
				//System.out.println("Is element null" + (element == null));
				if ((element != null) ) {
					sum += element.value;
				}
			}
		}
		return (double) ((double) sum /16);
	}
}
