package Game;

public class SimpleTile {
	public int value = 0;
	public Point slideTo;
	public boolean canCombine = true;
	public int x;
	public int y;
	public boolean marked;
	public boolean combineAnimation = false;
	public SimpleTile(SimpleTile a) {
		this.value = a.value;
		this.slideTo = new Point(a.x, a.y);
		this.x = a.x;
		this.y = a.y;
	
	}
	public SimpleTile(int value, int x,int y) {
		this.value = value;
		this.x = x;
		this.y = y;
		slideTo = new Point(x,y);
	}
	public int getValue() {
		return value;
	}
	public void setValue(int v) {
		this.value = v;
		//drawImage(); Overiding this in the subclass
	}
	public boolean canCombine() {
		return canCombine;
	}

	public void setCanCombine(boolean canCombine) {
		this.canCombine = canCombine;
	}
	public Point getSlideTo() {
		return slideTo;
	}

	public void setSlideTo(Point slideTo) {
		this.slideTo = slideTo;
	}
	public void setCombineAnimation(boolean combineAnimation) {
		this.combineAnimation = combineAnimation;
	}
}
