package Game;

public class vector {
	public int x;
	public int y;
	public vector (int a) {
		switch (a) {
		case 0: x = 0; y = -1;
			break;
		case 1: x = 1; y = 0;
			break;
		case 2: x = 0; y = 1;
			break;
		case 3: x = -1; y = 0;
			break;
		}
	}
}
