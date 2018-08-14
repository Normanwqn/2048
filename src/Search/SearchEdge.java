package Search;

public class SearchEdge {
	public final int cost = 1;
    public final SearchNode target;

    public SearchEdge(SearchNode targetNode){
            target = new SearchNode(targetNode);
    }
    
    
}
