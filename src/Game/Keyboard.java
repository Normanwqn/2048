package Game;
import java.awt.event.KeyEvent;

//import Search.DFS;

//Static Keyboard Class
public class Keyboard {
	public static boolean[] pressed = new boolean[256];
	public static boolean[] prev = new boolean[256];
	public static boolean[] AI_Input = new boolean[256];
	static String AINext;
	
	private Keyboard() {
		
	}
	public static void update() {
		/*for (int i = 0; i < 256; i++) {
			prev[i] = pressed[i];
		}*/
		/*prev[KeyEvent.VK_LEFT] = pressed[KeyEvent.VK_LEFT];
		prev[KeyEvent.VK_UP] = pressed[KeyEvent.VK_UP];
		prev[KeyEvent.VK_RIGHT] = pressed[KeyEvent.VK_RIGHT];
		prev[KeyEvent.VK_DOWN] = pressed[KeyEvent.VK_DOWN];*/
		for (int i = 0; i < 4; i++) {
			if (i == 0) prev[KeyEvent.VK_LEFT] = pressed[KeyEvent.VK_LEFT];
			if (i == 1) prev[KeyEvent.VK_RIGHT] = pressed[KeyEvent.VK_RIGHT];
			if (i == 2) prev[KeyEvent.VK_UP] = pressed[KeyEvent.VK_UP];
			if (i == 3) prev[KeyEvent.VK_DOWN] = pressed[KeyEvent.VK_DOWN];
		}
		
		for (int i = 0; i < 256; i++) { 
			AI_Input[i] = false;
		}
		/*new Thread(new Runnable() {
		@Override
		public void run() {
			AINext = Game.agent.nextMove();
		}
		}).start();*/
		AINext = Game.agent.nextMove();
		System.out.println("Moving " + AINext); 
		if (AINext.equals("Dead")) {
			Game.board.dead = true;
		}
		//System.out.println("H:"+ DFS.trialHeuristic(Game.board) );
		Game.board.printBoard();
		/*if (AINext != null) {
			switch (AINext) {
			case "LEFT": AI_Input[KeyEvent.VK_LEFT] = true; 
				//prev[KeyEvent.VK_LEFT] = true; pressed[KeyEvent.VK_LEFT] = false;
				break;
			case "RIGHT": AI_Input[KeyEvent.VK_RIGHT] = true;
				//prev[KeyEvent.VK_RIGHT] = true; pressed[KeyEvent.VK_RIGHT] = false;
				break;
			case "UP": AI_Input[KeyEvent.VK_KP_UP] = true;
				//prev[KeyEvent.VK_UP] = true; pressed[KeyEvent.VK_UP] = false;
				break;
			case "DOWN": AI_Input[KeyEvent.VK_DOWN] = true;
				//prev[KeyEvent.VK_DOWN] = true; pressed[KeyEvent.VK_DOWN] = false;
				break;
			}
		}*/
	}
	
	public static void keyPressed(KeyEvent e) {
		pressed[e.getKeyCode()] = true;
	}
	
	public static void keyReleased(KeyEvent e) {
		pressed[e.getKeyCode()] = false;
	}
	
	public static boolean typed(int keyEvent) {
		return (!pressed[keyEvent] && prev[keyEvent]) || AITyped(keyEvent);
		
	}
	
	public static boolean AITyped(int keyEvent) {
		if (AINext == null) {
			return false;
		}
		if (keyEvent == KeyEvent.VK_LEFT) {
			if (AINext.equals("LEFT")) {
				return true;
			}
		}
		if (keyEvent == KeyEvent.VK_RIGHT) {
			if (AINext.equals("RIGHT")) {
				return true;
			}
		}
		if (keyEvent == KeyEvent.VK_UP) {
			if (AINext.equals("UP")) {
				return true;
			}
		}
		if (keyEvent == KeyEvent.VK_DOWN) {
			if (AINext.equals("DOWN")) {
				return true;
			}
		}
		return false;
	}
	//public static 
}
