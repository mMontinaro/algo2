package upo.graph.implementation;

import java.util.LinkedList;

import upo.graph.implementation.AdjListUndirWeight.Vertex;

public class Stack implements Frangia<Vertex> {
	LinkedList<Vertex> stack;
	
	public Stack() {
		stack = new LinkedList<Vertex>();
	}
	
	@Override
	public void add(Vertex vertex) {
		stack.add(vertex);
	}

	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public Vertex getFirst() {
		return stack.getLast();
	}

	@Override
	public void removeFirst() {
		stack.removeLast();
	}

}
