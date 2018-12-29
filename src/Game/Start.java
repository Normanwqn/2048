package Game;
import javax.swing.JFrame;

public class Start {
	public static void main(String[] args) {
		Game game = new Game(); //Creating a new "Game"
		JFrame window = new JFrame("2048 Simulation"); //Creating new JFrame
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Default Close Operation
		window.setResizable(false); //Set the window to non-resizable
		window.add(game); //Add "game" to the JFrame
		window.pack();
		window.setLocationRelativeTo(null);//Centered on the Screen
		window.setVisible(true);
		
		game.start();
	}
}
