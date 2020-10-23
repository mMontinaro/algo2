package upo.graph.implementation;

import java.util.LinkedList;

import upo.graph.implementation.AdjListUndirWeight.Vertex;

public class Queue implements Frangia<Vertex>{
	
	LinkedList<Vertex> queue;
	
	public Queue() {
		queue = new LinkedList<Vertex>();
	}

	@Override
	public void add(Vertex vertex) {
		queue.addLast(vertex);
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public Vertex getFirst() {
		return queue.getFirst();
	}

	@Override
	public void removeFirst() {
		queue.removeFirst();
	}
}
