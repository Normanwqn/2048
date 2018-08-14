package Game;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JPanel;

import Search.AStarSearch;

//import com.normanwqn.A_Star_Search.AStarSearch;

import Search.DFS;
import Search.Expectiminimax;
import Search.SimpleGreedy;
import Search.minMax;

public class Game extends JPanel implements Runnable, KeyListener{

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 400;
	public static final int HEIGHT = 630;
	public static final Font main = new Font("Clear Sans", Font.PLAIN, 40);
	private Thread game; //Game Thread
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	//Double Buffering 
	public static SimpleGameBoard board;
	public static AI agent;
	public static WriteExcel excel = new WriteExcel();
	private long startTime;
	private long timeElapsed;
	private boolean set;
	private boolean running;
	public int timeRun = 0;
	public final int LIMIT = 1;
	public Game() {
		setFocusable(true); //Being able to receive input
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addKeyListener(this);
		board = new GameBoard(WIDTH/2 - GameBoard.BOARD_WIDTH/2, HEIGHT - GameBoard.BOARD_HEIGHT -10);
		
	}
	
	
	//@SuppressWarnings("deprecation")
	public void update() { 
		
		
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				b.update();
			}
		}).start();*/
		
		//agent.update(board);
		
		/*SimpleTile[][] testingTiles = new SimpleTile[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j<4; j++) {
				testingTiles[i][j] = new SimpleTile(0, i,j);
				testingTiles[i][j].setValue(0);
			}
		}*/
		//SimpleGameBoard testing = new SimpleGameBoard(testingTiles, 0);
		//testing.printBoard();
		//System.out.println("Is testing null? "+ (testing == null));
		/*new Thread(new Runnable() {
			@Override
			public void run() {
				GameBoard b = (GameBoard) board;
				b.update();
				agent.update(board);
				Keyboard.update();
			}
		}).start();*/
		GameBoard b = (GameBoard) board;
		b.update();
		agent.update(board);
		Keyboard.update();
		
		//a.stop();
		//agent.update(board);
	}
	public void reset() {
		board = new GameBoard(WIDTH/2 - GameBoard.BOARD_WIDTH/2, HEIGHT - GameBoard.BOARD_HEIGHT -10);
	}
	public void render() {
		//Graphics object implements how the image is drawn 
		//What needs to be drawn is stored in "image"
		//
				Graphics2D g = (Graphics2D) image.getGraphics();
				
				 g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				 g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
				 g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				 g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				 g.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
				 g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
				 g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
				 g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
				 g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE); 
			  
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, WIDTH, HEIGHT);
				//Render Board
				GameBoard b = (GameBoard) board;
				b.render(g);
				g.dispose();
				
				Graphics2D g2d = (Graphics2D) getGraphics();
				g2d.drawImage(image, 0, 0, null);
				g2d.dispose();
			
		
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		Keyboard.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		Keyboard.keyReleased(e);
	}
	public synchronized void start() {
		if (running) return;
		running = true;
		game = new Thread(this, "game");
		game.start();
		
	}
	public synchronized void stop(long timeSpent) {
		timeRun ++;
		if (running) return;
		running = false;
		System.out.println("The game has run " + timeRun +" time(s).");
		//Recording the data
		int endScore = this.board.score;
		int endMaxTile = this.board.findMax();
		int searchTime; int searchDepth;
		double averageTile = this.board.findAverage();
		String agentType = agent.getClass().getName();
		
		if (agentType.equals("Search.Expectiminimax")) {
			searchTime = Expectiminimax.delay;
			
		}
	
		excel.receiveOneGameStats(endMaxTile, endScore, averageTile, timeSpent);
		if ((timeRun %10 == 0)&&(timeRun != LIMIT)) {
			try {
				excel.printResult();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while (timeRun < LIMIT) {
			running = true;
			reset(); //resetting the board
			run();
		}
		
		try {
			excel.printResult();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.exit(0);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int fps = 0;
		int updates = 0;
		long fpsTimer = System.currentTimeMillis();
		//How many nanoseconds there are between updates
		double nsPerUpdate = 1000000000.0/5;
		//board.printBoard();
		System.out.println("Is board null? " +(board == null));
		agent = new Expectiminimax(board);
		excel.setType(agent.getClass().getName());
		//Last Update Time in nanoseconds
		long then = System.nanoTime();
		double unprocessed = 0; //How many updates there needs to be
		while (running) {
			/*long now = System.nanoTime();
			unprocessed += (double) ((now - then) / nsPerUpdate);
			then = now;
			
			//Determine whether rendering should be done
			boolean shouldRender = false;
			//Update Queue
			
			while (unprocessed >= 1) {
				updates ++;
				//update();
				new Thread(new Runnable() {
					@Override
					public void run() {
						update();
					}
				}).start();
				unprocessed --;
				shouldRender = true;
			}
			if (shouldRender) {
				fps ++;
				render();
				
				shouldRender = false;
			} else {
				try {
					Thread.sleep(1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}*/
			
			update();
			//update();
			
			render();
			//render();
			if (board.dead) {
				running = false;
				stop(System.nanoTime()-then);
			}
			//FPS Timer 
			//If the current time is larger than fpsTimer by 1s
			/*if (System.currentTimeMillis() - fpsTimer >1000) {
				System.out.printf("%d fps %d upates",fps, updates);
				System.out.println();
				fps = 0;
				updates = 0;
				fpsTimer += 1000;
			}*/
		}
		
		
	}

}
