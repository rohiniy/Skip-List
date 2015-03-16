package skipList;

public class FindResult <T>{

	private SkipNode<T> node;
	private SkipNode<T>[] visited;


	public SkipNode<T> getNode() {
		return node;
	}


	public void setNode(SkipNode<T> node) {
		this.node = node;
	}


	public SkipNode<T>[] getVisited() {
		return visited;
	}


	public void setVisited(SkipNode<T>[] visited) {
		this.visited = visited;
	}


	public FindResult(SkipNode<T> node, SkipNode<T>[] visited) {
		this.node = node;
		this.visited = visited;
	}
	

}
