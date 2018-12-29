package Game;

public abstract class AI {
	
	public SimpleGameBoard currentBoard;
	public SimpleGameBoard cloneofCurrent;
	
	
	public AI(SimpleGameBoard g) {
		// TODO Auto-generated constructor stub
		currentBoard = new SimpleGameBoard(g);
	}


	public Tile[][] current;
	public int currentScore;

	
	public abstract String nextMove();
		//System.out.println("Update");
		//currentBoard.printBoard();
		
	
	public void update(Tile[][] newBoard, int cS) {
		for (int i = 0; i < current.length; i++) {
			for (int j = 0; j < current.length; j++) {
				this.current[i][j] = current[i][j];
			}
		}
		this.currentScore = cS;
	}
	public void update(SimpleGameBoard gBoard) {
		currentBoard = gBoard;
		cloneofCurrent = new SimpleGameBoard(gBoard);
		
	}
}
