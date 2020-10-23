package upo.graph.implementation;

public interface Frangia <V>{
	public void add(V vertex);
	public boolean isEmpty();
	public V getFirst();
	public void removeFirst();
}
