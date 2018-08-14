package Game;

public class Point {
	public int row;
	public int col;
	
	public Point(int row, int col) {
		this.row = row;
		this.col = col;
	}
	public Point(Point cell) {
		// TODO Auto-generated constructor stub
		this.row = cell.row;
		this.col = cell.col;
	}
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
}
