package Game;
import java.util.Random;

public class dumbTestingAI extends AI {
	
	
	/*public dumbTestingAI(Tile[][] current, int cS) {
		//super(current, cS);
		// TODO Auto-generated constructor stub
	}*/
	
	public dumbTestingAI(SimpleGameBoard g) {
		super(g);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public String nextMove() {
		//super.nextMove();
		Random random = new Random();
		String AIMove ="";
		SimpleGameBoard testingBoard = new SimpleGameBoard(currentBoard);
		System.out.println("Testing Board:");
		testingBoard.moveTiles(Direction.LEFT);
		testingBoard.printBoard();
		/*
		switch(random.nextInt(4)) {
		case 0: AIMove = "LEFT";
			break;
		case 1: AIMove = "RIGHT";
			break;
		case 2: AIMove = "UP";
			break;
		case 3: AIMove = "DOWN";
			break;
		}*/
		AIMove = "LEFT";
		return AIMove;
	} 
}
