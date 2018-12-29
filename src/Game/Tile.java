package Game;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Tile extends SimpleTile{
	public static final int WIDTH = 80;
	public static final int HEIGHT = 80;
	public static final int SLIDE_SPEED = 30;
	public static final int ARC_WIDTH = 5;
	public static final int ARC_HEIGHT = 5;
	
	//private int value;
	private BufferedImage tileImage;
	private Color background;
	private Color text;
	private Font font;
	//private Point slideTo;
	//private int x;
	//private int y;
	//If it is going to play the Animation
	private boolean beginningAnimation = true;
	private double scaleFirst = 0.1; //Keep scaling up every update
	private BufferedImage beginningImage;
	
	private double scaleCombine = 1.2;
	private BufferedImage combineImage;
	
	//private boolean canCombine = true;
	
	public Tile(int value, int x, int y) {
		super(value, x, y);
		//this.value = value;
		//this.x = x;
		//this.y = y;
		//slideTo = new Point(x,y); //Slide to whatever planned point
		tileImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		beginningImage = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_ARGB);
		combineImage = new BufferedImage(WIDTH*2,HEIGHT*2, BufferedImage.TYPE_INT_ARGB);
		drawImage();
	}
	public Tile(int value, int x, int y, boolean bi, boolean bc) {
		/*this.value = value;
		this.x = x;
		this.y = y;
		slideTo = new Point(x,y); //Slide to whatever planned point*/
		super(value, x, y);
		tileImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		beginningImage = new BufferedImage(WIDTH,HEIGHT, BufferedImage.TYPE_INT_ARGB);
		combineImage = new BufferedImage(WIDTH*2,HEIGHT*2, BufferedImage.TYPE_INT_ARGB);
		beginningAnimation = bi;
		combineAnimation = bc; 
		drawImage();
	}
	/*public int getValue() {
		return value;
	}*/
	@Override
	public void setValue(int v) {
		super.setValue(v);
		drawImage();
	}
	public void drawImage() {
		Graphics2D g = (Graphics2D) tileImage.getGraphics();
		 RenderingHints rh = new RenderingHints(
	             RenderingHints.KEY_TEXT_ANTIALIASING,
	             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		 RenderingHints ab = new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		 g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	      g.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
	      g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	      g.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
	      g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	      g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
	      g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	      g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE);   
		text = Color.WHITE;
		if ((value == 2) || (value == 4)) {
			text = new Color(118,110,102);
		}
		switch (value) {
			case 2: background = new Color(235,228,219);//2
				break;
			case 4: background = new Color(234,223,202); //4
				break;
			case 8: background = new Color(232,179,129);  //8
				break;
			case 16: background = new Color(232,153,108); //16
				break;
			case 32: background = new Color(226,132,102); //32
				break;
			case 64: background = new Color(215,99,65); //64
				break;
			case 128: background = new Color(239,216,124); //128
				break;
			case 256: background = new Color(234,209,101); //256
				break;
			case 512: background = new Color(224,192,75); //512
				break;
			case 1024: background = new Color(229,195,90); //1024
				break;
			case 2048: background = new Color(230,196,66); //2048
				break;
			case 4096: background = new Color(229,69,203); //4096
				break;
			case 8192: background = new Color(219,89,93); //8192
				break;
			case 16384: background = new Color(229,71,159); //16384
				break;
			case 32768: background = new Color(165,181,84); //32768
				break;
			case 65536: background = new Color(109,159,216); //65536
				break;
			case 131072: background = new Color(52,124,183); //131072
				break;
			default: background = Color.BLACK;
				break;
		}
		g.setColor(new Color(0,0,0,0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(background);
		//ystem.out.println(background.getBlue());
		g.fillRoundRect(0, 0, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);
		font = Game.main;
		g.setFont(font);
		//Getting the width
		int drawX = WIDTH / 2 - DrawUtils.getMessageWidth("" + value, font, g)/2;
		//Getting the height of the text; Need to change to +
		int drawY = HEIGHT /2 + DrawUtils.getMessageHeight(""+value, font, g)/2;
		g.setColor(text);
		g.drawString(""+value, drawX, drawY);
		g.dispose();
	}
	public void update() {
		//double t = 0.01;
		if (beginningAnimation) {
			AffineTransform transform = new AffineTransform();
			//Find the size of the transformed image
			transform.translate(WIDTH/2 - scaleFirst* WIDTH/2, HEIGHT/2 - scaleFirst* HEIGHT/2); //Go to the middle; 
			transform.scale(scaleFirst, scaleFirst); 
			Graphics2D g2d = (Graphics2D) beginningImage.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE); 
			g2d.setColor(background);
			g2d.fillRect(0, 0, WIDTH, HEIGHT);
			g2d.drawImage(tileImage, transform, null);
			
			/*while (scaleFirst < 3) {
				scaleFirst +=t*t*(3-2*t);
				transform.scale(scaleFirst, scaleFirst);
				g2d.drawImage(tileImage, transform, null);
				t = t+0.001;
			}*/
			scaleFirst +=0.1;
			//scaleFirst *= (1+0.007);
			g2d.dispose();
			//beginningAnimation = false;
			if (scaleFirst >= 1) beginningAnimation = false;
		} else if (combineAnimation) {
			AffineTransform transform = new AffineTransform();
			//Find the size of the transformed image
			transform.translate(WIDTH/2 - scaleCombine* WIDTH/2, HEIGHT/2 - scaleCombine* HEIGHT/2);
			transform.scale(scaleCombine, scaleCombine);
			Graphics2D g2d = (Graphics2D) combineImage.getGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 100);
			g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_PURE); 
			g2d.setColor(background);
			g2d.fillRect(0, 0, WIDTH, HEIGHT);
			g2d.drawImage(tileImage, transform, null);
			double t = 0.1;
			/*while (scaleCombine > 0) {
				scaleCombine = (t*t*(3-2*t));
				transform.scale(scaleCombine, scaleCombine);
				g2d.drawImage(tileImage, transform, null);
				t += 0.001;
			}*/
			//scaleCombine /= (1+0.007);
			g2d.dispose();
			//combineAnimation = false;
			
			scaleCombine -=(t*t*(3-2*t));
			t+=0.01;
			if (scaleCombine <= 1) combineAnimation = false;
		}
	}
	public void render(Graphics2D g) {
		/*if (beginningAnimation) {
			g.drawImage(beginningImage, x, y, null);
		} else if (combineAnimation) {
			g.drawImage(combineImage, (int) (x + WIDTH/2 - scaleCombine*WIDTH/2), (int) (y + HEIGHT/2 - scaleCombine*HEIGHT/2), null);
			//Bigger than 
		} else {
			g.drawImage(tileImage, x, y, null);
		}*/
		g.drawImage(tileImage, x, y, null);
	}

	/*public boolean canCombine() {
		return canCombine;
	}

	public void setCanCombine(boolean canCombine) {
		this.canCombine = canCombine;
	}*/

	/*public Point getSlideTo() {
		return slideTo;
	}

	public void setSlideTo(Point slideTo) {
		this.slideTo = slideTo;
	}*/
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

	public boolean isCombineAnimation() {
		return combineAnimation;
	}

	
}