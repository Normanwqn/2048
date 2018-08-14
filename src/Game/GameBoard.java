package Game;
import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class GameBoard extends SimpleGameBoard {
	/*public static final int ROWS = 4;
	public static final int COLS = 4;
	private final int startingTiles = 2;
	private Tile[][] board;
	private boolean dead; //end of game
	private boolean won; //Won the game*/
	private BufferedImage gameBoard;
	private BufferedImage finalBoard;
	private int x;
	private int y;
	//private int score = 0;
	//private int highScore = 0; //Highest Score
	private Font scoreFont;
	
	private long elapsedMS;
	private long fastestMS;
	private long startTime;
	//private boolean hasStarted;
	private String formattedTime = "00:00:000";
	
	private static int SPACING = 10;
	//There is spacing before the first tile, thus COLS+1; 
	public static int BOARD_WIDTH = (COLS + 1)* SPACING + COLS * Tile.WIDTH;
	public static int BOARD_HEIGHT = (ROWS+1) * SPACING + ROWS* Tile.HEIGHT;
	//Saving 
	private String saveDataPath;
	private String fileName ="SaveData";
	public static AI agent;
	
	public GameBoard(int x, int y)  {
		super();
		try  {
			saveDataPath = GameBoard.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			saveDataPath = "/Volumes/GoogleDrive/My Drive/Java/2048/src/";
		} catch (Exception e) {
			e.printStackTrace();
		}
		scoreFont = Game.main.deriveFont(24f);
		
		this.x = x;
		this.y = y;
		//board = new Tile[ROWS][COLS];
		gameBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		finalBoard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		startTime = System.nanoTime();
		createBoardImage(); //Draw the background of the Board
		loadHighScore();
		
		//agent = new dumbTestingAI(new SimpleGameBoard(this, this.getScore()));
		start();
	}
	private void createSaveData() {
		//Creating files
		try {
			File file = new File(saveDataPath, fileName);
			FileWriter output = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(output);
			writer.write("" + 0);
			writer.newLine();
			writer.write("" + Integer.MAX_VALUE);
			//Create Fastest Time
			writer.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void loadHighScore() {
		try {
			File f = new File(saveDataPath, fileName);
			if (!f.isFile()){
				createSaveData();
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			highScore = Integer.parseInt(reader.readLine());
			//Read fastest time
			fastestMS = Long.parseLong(reader.readLine());
			reader.close();
			
		}catch (Exception e) {
			e.printStackTrace();;
		}
	}
	private void setHighScore() {
		FileWriter output = null;
		try {
			File f = new File(saveDataPath, fileName);
			output = new FileWriter(f);
			BufferedWriter writer = new BufferedWriter(output);
			
			writer.write("" + highScore);
			writer.newLine();
			if (elapsedMS <= fastestMS && won) {
				writer.write(""+elapsedMS);
			} else {
				writer.write(""+fastestMS);
			}
			
			writer.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void createBoardImage() {
		Graphics2D g = (Graphics2D) gameBoard.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
	    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
	    g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	    g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	    g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE); 
		g.setColor(new Color(185,173,162));
		g.fillRoundRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
		g.setColor(new Color(202,193,181));
		
		for (int row = 0; row < ROWS; row ++) {
			for (int col = 0; col < COLS; col++) {
				int x_rect = SPACING + SPACING * col + Tile.WIDTH*col;
				int y_rect = SPACING + SPACING * row + Tile.HEIGHT*row;
				g.fillRoundRect(x_rect, y_rect, Tile.WIDTH, Tile.HEIGHT, Tile.ARC_WIDTH, Tile.ARC_HEIGHT);
			}
		}
	}
	private void start() {
		for (int i = 0; i < startingTiles; i++) {
			spawnRandom();
		}
	}
	private void spawnRandom() {
		Random random = new Random();
		boolean notValid = true;
		while (notValid) {
			int location= random.nextInt(ROWS*COLS);
			int row = location / ROWS;
			int col = location % COLS;
			Tile current = (Tile) board[row][col];
			if (current == null) {
				int value = random.nextInt(10) < 9? 2:4;
				Tile tile = new Tile(value, getTileX(col), getTileY(row));
				board[row][col] = tile;
				notValid = false;
			}
			
		}
	}
	public int getTileX(int col) {
		return SPACING + col*Tile.WIDTH + col*SPACING;
	}
	public int getTileY(int row) {
		return SPACING + row*Tile.HEIGHT + row*SPACING;
	}
	
	//Continously rendering the board
	public void render(Graphics2D g) {
		Graphics2D g2d = (Graphics2D) finalBoard.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	      g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
	      g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	      g.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
	      g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	      g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	      g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	      g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);   
	      g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	      g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
	      g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	      g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
	      g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	      g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	      g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	      g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE); 
		g2d.drawImage(gameBoard, 0, 0, null);
		//Draw Tiles
		for (int row = 0; row < ROWS; row ++) {
			for (int col = 0; col < COLS; col++) {
				Tile current = (Tile) board[col][row];
				if (current == null) {
					continue;
				}
				current.render(g2d);
			}
		}
		//Drawing to the Screen
		g.drawImage(finalBoard,	x, y, null);
		g2d.dispose();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	      g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
	      g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	      g.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
	      g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	      g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	      g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	      g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);  
		g.setColor(Color.GRAY);
		g.setFont(scoreFont);
		g.drawString(""+score, 30, 40);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString("Best:" + highScore, Game.WIDTH- 30-DrawUtils.getMessageWidth("Best:" + highScore, scoreFont, g), 40);
		g.setColor(Color.GRAY);
		g.drawString("Time:" + formattedTime, 30, 90);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString("Fastest: " + formatTime(fastestMS),30, 130);
		
	}
	
	public void update() {
		if (!won && !dead) {
			if (hasStarted) {
				elapsedMS = (System.nanoTime()-startTime)/(1000000);
				formattedTime = formatTime(elapsedMS);
			} else {
				startTime = System.nanoTime();
			}
		}
		checkKeys();
		
		if (score >= highScore) {
			highScore = score;
		}
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Tile current = (Tile) board[row][col];
				if (current == null) continue;
				current.update();
				//Reset position; Slide across the screen
				resetPosition(current, row, col);
				if (current.getValue() == 2048) {
					won = true;
				}
				
			}
		}
	}
	public static String formatTime(long millis) {
		String formattedTime;
		String hourFormat = "";
		int hours = (int) (millis /3600000);
		if (hours >=1) {
			millis -= hours*3600000;
			if (hours <10) {
				hourFormat = "0" + hours;
			} else {
				hourFormat = ""+ hours;
			}
			hourFormat += ":";
		}
		String minuteFormat;
		int minutes = (int) (millis /60000);
		if (minutes >= 1) {
			millis -= minutes*60000;
			if (minutes < 10) {
				minuteFormat = "0" + minutes;
			} else {
				minuteFormat = "" + minutes;
			}
		} else {
			minuteFormat = "00";
		}
		String secondFormat;
		int seconds = (int) (millis /1000);
		if (seconds >= 1) {
			millis -= seconds*1000;
			if (seconds < 10) {
				secondFormat = "0" + seconds;
			} else {
				secondFormat = "" + seconds;
			}
		} else {
			secondFormat = "00";
		}
		String millisFormat;
		if (millis >99) {
			millisFormat ="" + millis;
		}else if (millis >9){
			millisFormat ="0" + millis;
		} else {
			millisFormat ="00" + millis;
		}
		formattedTime = hourFormat + minuteFormat + ":" + secondFormat + "." + millisFormat;
		return formattedTime;
	}
	private void resetPosition(Tile current, int row, int col) {
		if (current == null) return; //If current does not exist
		int x = getTileX(col);
		int y = getTileY(row);
		
		int distX = current.getX() - x;
		int distY = current.getY() - y;
		if (Math.abs(distX) < Tile.SLIDE_SPEED) {
			current.setX(current.getX() - distX); //x
		} 
		if (Math.abs(distY) < Tile.SLIDE_SPEED) {
			current.setY(current.getY() - distY); //y
		}
		if (distX < 0) {
			current.setX(current.getX() + Tile.SLIDE_SPEED);
		}
		if (distY < 0) {
			current.setY(current.getY() + Tile.SLIDE_SPEED);
		}
		if (distX > 0) {
			current.setX(current.getX() - Tile.SLIDE_SPEED);
		}
		if (distY > 0) {
			current.setY(current.getY() - Tile.SLIDE_SPEED);
		}
	}

	@Override
	public boolean moveTiles(Direction dir) {
		boolean canMove = super.moveTiles(dir);
		if (canMove) {
			spawnRandom();
		}
		//printBoard();
		return true; 
	}
	@Override
	public void checkDead() {
		super.checkDead();
		//System.out.println("Dead? " + dead);
		setHighScore();
		//setHighScore; Timer...
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}
