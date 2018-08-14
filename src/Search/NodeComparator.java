package Search;
import java.util.Comparator;

import Game.Node;
import Game.SimpleGameBoard;

public class NodeComparator implements Comparator<Node>{

	@Override
	public int compare(Node o1, Node o2) {
		// TODO Auto-generated method stub
		if (o1.totalCost < o2.totalCost) {
			return -1;
		} else if (o1.totalCost > o2.totalCost) {
			return 1;
		}
		return 0;
	}
	
}
