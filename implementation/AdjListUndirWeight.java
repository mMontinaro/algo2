package upo.graph.implementation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;
import upo.graph.base.*;
import upo.graph.base.VisitForest.Color;
import upo.graph.base.VisitForest.VisitType;

/**
 * Implementazione mediante <strong>liste di adiacenza</strong> di un grafo <strong>non orientato pesato</strong>.
 * 
 * @author Matteo Montinaro 20023069
 *
 */
public class AdjListUndirWeight implements WeightedGraph{
	
	class Vertex{
		//value or name given to vertex
		int index;
		LinkedList<Adjacent> adjList = new LinkedList<Adjacent>();
		
		public Vertex() {}
		
		public Vertex(int i) {
			setIndex(i);
		}
		
		int getIndex() {
			return this.index;
		}
		
		void setIndex(int i) {
			this.index = i;
		}
		
		void addAdj(Adjacent v) {
			this.adjList.add(v);
		}
		
		int getAdjListSize() {
			return this.adjList.size();
		}
	}
	
	LinkedList<Vertex> VList;
	
	public AdjListUndirWeight(){
		VList = new LinkedList<Vertex>();
	}
	
	Vertex getVertex(int i) {
		return (VList.get(i));
	}
	
	@Override
	public int addVertex() {
		VList.add(new Vertex(size()));
		if(size() == 0) return 0;
		else return size()-1;
	}

	@Override
	public boolean containsVertex(int index) {
		if(index >= 0 && index < size()) return true;
		return false;
	}

	@Override
	public void removeVertex(int index) throws NoSuchElementException {
		if(containsVertex(index)) {
			this.VList.remove(index);
			for(int i = index; i < this.VList.size(); i++) {
				this.getVertex(i).setIndex(i);
			}
		} else throw new NoSuchElementException("Vertex does not exist");
	}

	class Adjacent{
		//vertice cui e` connesso e il peso dell'arco
		Vertex adj;
		double weight;
		
		public Adjacent(Vertex ver1){
			this.adj = ver1;
		}
		
		public double getWeight() {
			return this.weight;
		}
		
		public Vertex getAdj() {
			return this.adj;
		}
		
	}
	
	@Override
	public void addEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException {
		if(containsVertex(sourceVertexIndex) && containsVertex(targetVertexIndex)) {
			getVertex(sourceVertexIndex).addAdj(new Adjacent(getVertex(targetVertexIndex)));
			getVertex(targetVertexIndex).addAdj(new Adjacent(getVertex(sourceVertexIndex)));
		} else throw new IllegalArgumentException("Source or Target Vertex does not exist");
	}

	@Override
	public boolean containsEdge(int sourceVertexIndex, int targetVertexIndex) throws IllegalArgumentException {
		if(containsVertex(sourceVertexIndex) && containsVertex(targetVertexIndex)) {
			//for every Adjacent element within the list of adjacents of a vertex
			for(Adjacent a : getVertex(sourceVertexIndex).adjList) {
				if(a.adj.index == targetVertexIndex)
					return true;
			}
		} else throw new IllegalArgumentException("Source or Target Vertex does not exist");
		return false;
	}

	@Override
	public void removeEdge(int sourceVertexIndex, int targetVertexIndex)
			throws IllegalArgumentException, NoSuchElementException {
		if(containsVertex(sourceVertexIndex) && containsVertex(targetVertexIndex)) {
			if(containsEdge(sourceVertexIndex, targetVertexIndex)) {
				for(Adjacent a : getVertex(sourceVertexIndex).adjList) {
					if(a.adj.index == targetVertexIndex) {
						getVertex(sourceVertexIndex).adjList.remove(a);
					}
				}
				for(Adjacent a : VList.get(targetVertexIndex).adjList) {
					if(a.adj.index == sourceVertexIndex) {
						getVertex(targetVertexIndex).adjList.remove(a);
					}
				}
			}
			else throw new NoSuchElementException("Edge between these two Vertexes does not exist");
		} else throw new IllegalArgumentException("Source or Target Vertex does not exist");
	}

	@Override
	public Set<Integer> getAdjacent(int vertexIndex) throws NoSuchElementException {
		Set<Integer> hashSet = new HashSet<Integer>();
		
		if(containsVertex(vertexIndex)) {
			for(Adjacent a : getVertex(vertexIndex).adjList) {
				hashSet.add(a.adj.index);
			}
		}
		else throw new NoSuchElementException("Vertex does not exist");
		
		return hashSet;
	}

	@Override
	public boolean isAdjacent(int targetVertexIndex, int sourceVertexIndex) throws IllegalArgumentException {
		if(!VList.contains(getVertex(sourceVertexIndex)))
			throw new IllegalArgumentException("Source Vertex does not exist");
		
		return containsEdge(sourceVertexIndex, targetVertexIndex);
	}
	

	@Override
	public int size() {
		return VList.size();
	}

	@Override
	public boolean isDirected() {
		return false;
	}
	
	private VisitForest visitaGenerica(Vertex startingVertex, Frangia<Vertex> f, VisitForest handler) {
		int time = 0;
		handler.setColor(startingVertex.index, Color.GRAY);
		handler.setStartTime(startingVertex.index, time);
		time++;
		f.add(startingVertex);
		
		while(!f.isEmpty()) {
			Vertex u = f.getFirst();
			Vertex v = new Vertex(-1);
			Set<Integer> adjList = this.getAdjacent(u.index);
			
			for(Integer b : adjList) {
				if(handler.getColor(b) == Color.WHITE) {
					v.setIndex(b);
					continue;
				}
			}
			
			if(v.getIndex() != -1) {
				handler.setColor(v.getIndex(), Color.GRAY);
				handler.setStartTime(v.getIndex(), time);
				time++;
				handler.setParent(v.getIndex(), u.getIndex());
				f.add(v);
			}
			
			else {
				handler.setColor(u.getIndex(), Color.BLACK);
				handler.setEndTime(u.getIndex(), time);
				time++;
				f.removeFirst();
			}
		}
		return handler;
	}

	@Override
	public boolean isCyclic() {
		VisitForest handler = new VisitForest(this, VisitType.DFS);
		for(Vertex u : VList) {
			if(handler.getColor(u.index) == Color.WHITE && visitaRicCiclo(u.getIndex(), handler))
				return true;
		}
		return false;
	}
	
	private boolean visitaRicCiclo(int sourceVertexIndex, VisitForest handler) {
		handler.setColor(sourceVertexIndex, Color.GRAY);	//sets the color of given vertex to gray
		Set<Integer> adjList = getAdjacent(sourceVertexIndex);	//creates set of adjacents
		for(int i : adjList) {	//for every element within the set
			if(handler.getColor(i) == Color.WHITE) {	//if color of adjacent is white
				handler.setParent(i, sourceVertexIndex);	//then parent is set
				if(visitaRicCiclo(i, handler)) return true;	//call recursively this method
			}
			else if(i != handler.getParent(sourceVertexIndex)) return true;	//if parent of given vertex is not in the list 
		}
		handler.setColor(sourceVertexIndex, Color.BLACK);
		return false;
	}

	@Override
	public boolean isDAG() {
		return false;
	}

	@Override
	public VisitForest getBFSTree(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		if(!VList.contains(getVertex(startingVertex)))
			throw new IllegalArgumentException("Source Vertex does not exist");
		//if(grafo non supporta visita)	throw new UnsupportedOperationException("the graph does not support this type of visit");
		return visitaGenerica(getVertex(startingVertex), new Queue(), new VisitForest(this, VisitType.BFS));
	}

	@Override
	public VisitForest getDFSTree(int startingVertex) throws UnsupportedOperationException, IllegalArgumentException {
		if(!VList.contains(getVertex(startingVertex)))
			throw new IllegalArgumentException("Source Vertex does not exist");
		//if(grafo non supporta visita)	throw new UnsupportedOperationException("the graph does not support this type of visit");
		return visitaGenerica(getVertex(startingVertex), new Stack(), new VisitForest(this, VisitType.DFS));
	}

	@Override
	public VisitForest getDFSTOTForest(int startingVertex)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("This graph does not support this visit");
		/*if(!VList.contains(getVertex(startingVertex)))
			throw new IllegalArgumentException("Source Vertex does not exist");
		
		VisitForest handler = new VisitForest(this, VisitType.DFS_TOT);
		for(Vertex u : VList) {
			if(handler.getColor(u.getIndex()) == Color.WHITE) {
				visitaGenerica(getVertex(startingVertex), new Stack(), handler);
			}
		}
		return handler;*/
	}

	@Override
	public VisitForest getDFSTOTForest(int startingVertex, int[] vertexOrdering)
			throws UnsupportedOperationException, IllegalArgumentException {
		throw new UnsupportedOperationException("This graph does not support this type of visit");
	}

	@Override
	public int[] topologicalSort() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This graph does not support this type of visit");
	}

	@Override
	public Set<Set<Integer>> stronglyConnectedComponents() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("This graph does not support this type of visit");
	}

	@Override
	public Set<Set<Integer>> connectedComponents() throws UnsupportedOperationException {
		//useless but is fashionable
		if(isDirected()) throw new UnsupportedOperationException("This graph does not support this type of visit");
		
		Set<Set<Integer>> hashSet = new HashSet<Set<Integer>>();
		VisitForest handler = new VisitForest(this, VisitType.DFS);
		
		for(Vertex u : VList) {
			Set<Integer> disc = new HashSet<Integer>();
			if(handler.getColor(u.getIndex()) == Color.WHITE) {
				getDiscendants(u.getIndex(), handler, disc);
				//System.out.println(disc);
				hashSet.add(disc);
			}
		}
		return hashSet;
	}
	
	public void getDiscendants(int index, VisitForest handler, Set<Integer> disc){	
		handler.setColor(index, Color.GRAY);
		disc.add(index);
		for(Integer i : getAdjacent(index)) {
			if(handler.getColor(i) == Color.WHITE) {
				handler.setParent(i, index);
				getDiscendants(i, handler, disc);
			}
		}
		handler.setColor(index, Color.BLACK);
	}
	
	public void printCC(Set<Set<Integer>> h) {
		h.toString();
	}
	
	public void printStatus() {
		for(Vertex v : VList) {
			System.out.print(v.getIndex() + " -> ");
			for(Adjacent a : v.adjList)
				System.out.print(a.adj.getIndex() + ", " + a.getWeight() + " -> ");
			System.out.println("~");
		}
	}

	@Override
	public double getEdgeWeight(int sourceVertexIndex, int targetVertexIndex)
			throws IllegalArgumentException, NoSuchElementException {
		if(containsVertex(sourceVertexIndex) && containsVertex(targetVertexIndex)) {
			if(containsEdge(sourceVertexIndex, targetVertexIndex)) {
				for(int i = 0; i < VList.get(sourceVertexIndex).adjList.size(); i++) {
					if(VList.get(sourceVertexIndex).adjList.get(i).adj.index == targetVertexIndex) {
						return VList.get(sourceVertexIndex).adjList.get(targetVertexIndex).weight;
					}
					if(VList.get(targetVertexIndex).adjList.get(i).adj.index == sourceVertexIndex) {
						return VList.get(targetVertexIndex).adjList.get(sourceVertexIndex).weight;
					}
				}
			} else throw new NoSuchElementException("Edge between the two Vertexes does not exist");
		} else throw new IllegalArgumentException("Source or Target Vertex does not exist");
		return 0;
	}

	@Override
	public void setEdgeWeight(int sourceVertexIndex, int targetVertexIndex, double weight)
			throws IllegalArgumentException, NoSuchElementException {
		if(containsVertex(sourceVertexIndex) && containsVertex(targetVertexIndex)) {
			if(containsEdge(sourceVertexIndex, targetVertexIndex)) {
				for(int i = 0; i < getVertex(sourceVertexIndex).adjList.size(); i++) {
					if(getVertex(sourceVertexIndex).adjList.get(i).adj.index == targetVertexIndex) {
						getVertex(sourceVertexIndex).adjList.get(i).weight = weight;
						continue;
					}
				}
				for(int i = 0; i < getVertex(targetVertexIndex).adjList.size(); i++) {
					if(getVertex(targetVertexIndex).adjList.get(i).adj.index == sourceVertexIndex) {
						getVertex(targetVertexIndex).adjList.get(i).weight = weight;
						continue;
					}
				}
			} else throw new NoSuchElementException("Edge between the two Vertexes does not exist");
		} else throw new IllegalArgumentException("Source or Target Vertex does not exist");
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof AdjListUndirWeight))
			return false;
		else {
			AdjListUndirWeight c = (AdjListUndirWeight) o;
			if(size() != c.size()) return false;
			for(int i = 0; i < size(); i++)
				if(VList.get(i).index != c.VList.get(i).index) return false;
			return true;
		}			
	}
	
	private class Edge {
		private Vertex vertex1;
		private Vertex vertex2;
		private double weight;
		
		public Edge(Vertex v1, Vertex v2, double w){
			this.vertex1 = v1;
			this.vertex2 = v2;
			this.weight = w;
		}
	}
	
	private LinkedList<Edge> FEList()
	{
		LinkedList<Edge> TuttiArchi = new LinkedList<Edge>();
		for(Vertex u: VList) {
			for(Adjacent v: u.adjList) {
				TuttiArchi.add(new Edge(u, v.getAdj(), v.getWeight()));
			}
		}
		return TuttiArchi;
	}
	
	protected void printFEList() {
		LinkedList<Edge> l = FEList();
		
		for(Edge e: l) {
			System.out.println("(" + e.vertex1.getIndex() + ", " + e.vertex2.getIndex() + ", " + e.weight + ")");
		}
	}
}
