package upo.graph.implementation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import upo.graph.base.VisitForest;
import upo.graph.base.VisitForest.VisitType;

class AdjListUndirWeightTest {
	
	AdjListUndirWeight grafo;
	
	@BeforeEach
	void setUp() throws Exception {
		grafo = new AdjListUndirWeight();
	}

	@Test
	void testAddVertex() {
		int v = grafo.addVertex();
		assertEquals(0, v, "diversi");
		v = grafo.addVertex();
		assertEquals(1, v, "diversi");
	}

	@Test
	void testContainsVertex() {
		int v = grafo.addVertex();
		assertEquals(0, v, "La matrice ha dimensione diversa");
		v = grafo.addVertex();
		assertEquals(1, v, "La matrice ha dimensione diversa");
		assertEquals(true, grafo.containsVertex(0));
		assertEquals(true, grafo.containsVertex(1));
		assertEquals(false, grafo.containsVertex(2));
	}

	@Test
	void testRemoveVertex() {
		int a = grafo.addVertex();
		assertEquals(true, grafo.containsVertex(a));
		grafo.addVertex();
		assertEquals(true, grafo.containsVertex(1));
		grafo.removeVertex(1);
		assertEquals(false, grafo.containsVertex(1));
	}

	@Test
	void testAddEdge() {
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addEdge(0, 1);
		grafo.addEdge(1, 2);
		grafo.addEdge(2, 0);
	}

	@Test
	void testContainsEdge() {
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addEdge(0, 1);
		grafo.addEdge(1, 2);
		grafo.addEdge(2, 0);
		assertEquals(true, grafo.containsEdge(0, 1), "l'arco non esiste");
		assertEquals(true, grafo.containsEdge(2, 0), "l'arco non esistente");
		assertEquals(true, grafo.containsEdge(2, 1), "l'arco non esistente");
		assertEquals(false, grafo.containsEdge(0, 3));
	}

	@Test
	void testRemoveEdge() {
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addEdge(0, 1);
		grafo.addEdge(1, 2);
		grafo.addEdge(2, 0);
		assertEquals(true, grafo.containsEdge(0, 1), "Arco inesistente");
		assertEquals(true, grafo.containsEdge(1, 2), "Arco inesistente");
		assertEquals(true, grafo.containsEdge(2, 0), "Arco inesistente");
		grafo.removeEdge(0, 1);
		grafo.removeEdge(1, 2);
		grafo.removeEdge(2, 0);
		assertEquals(false, grafo.containsEdge(0, 1), "Arco inesistente");
		assertEquals(false, grafo.containsEdge(1, 2), "Arco inesistente");
		assertEquals(false, grafo.containsEdge(2, 0), "Arco esistente");
	}

	@Test
	void testGetAdjacent() {
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addEdge(0, 1);
		grafo.addEdge(1, 2);
		grafo.addEdge(2, 0);
		Set <Integer> a = grafo.getAdjacent(0);
		Set <Integer> b = grafo.getAdjacent(1);
		Set <Integer> c = grafo.getAdjacent(2);
		assertEquals(false, a.contains(0));
		assertEquals(true, a.contains(1));
		assertEquals(true, a.contains(2));
		
		assertEquals(true, b.contains(0));
		assertEquals(false, b.contains(1));
		assertEquals(true, b.contains(2));
		
		assertEquals(true, c.contains(0));
		assertEquals(true, c.contains(1));
		assertEquals(false, c.contains(2));
	}

	@Test
	void testIsAdjacent() {
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addEdge(0, 1);
		grafo.addEdge(1, 2);
		assertEquals(true, grafo.isAdjacent(0, 1));
		assertEquals(true, grafo.isAdjacent(1, 2));
		assertEquals(false, grafo.isAdjacent(2, 0));
	}

	@Test
	void testSize() {
		grafo.addVertex();
		grafo.addVertex();
		assertEquals(2, grafo.size());
	}

	@Test
	void testIsDirected() {
		assertEquals(false, grafo.isDirected());
	}

	@Test
	void testIsCyclic() {
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addEdge(0, 1);
		grafo.addEdge(1, 2);
		grafo.addEdge(2, 3);
		grafo.addEdge(1, 3);
		assertEquals(true, grafo.isCyclic());
	}

	@Test
	void testIsDAG() {
		assertEquals(false, grafo.isDAG());
	}
	
	@Test
	void testConnectedComponents() {
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addEdge(0, 1);
		grafo.addEdge(1, 2);
		grafo.addEdge(3, 4);
		//print of the result
		//grafo.printCC(grafo.connectedComponents());
		Set<Integer> h1 = new HashSet<Integer>();
		h1.add(0);
		h1.add(1);
		h1.add(2);
		Set<Integer> h2 = new HashSet<Integer>();
		h2.add(3);
		h2.add(4);
		Set<Set<Integer>> tmp = new HashSet<Set<Integer>>();
		tmp.add(h1);
		tmp.add(h2);
		assertEquals(tmp, grafo.connectedComponents());
	}
	
	@Test
	void testEquals() {
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addEdge(1, 3);
		grafo.addEdge(0, 1);
		grafo.addEdge(0, 2);
		grafo.addEdge(0, 3);
		
		AdjListUndirWeight g2 = new AdjListUndirWeight();
		g2.addVertex();
		g2.addVertex();
		g2.addVertex();
		g2.addVertex();
		g2.addEdge(1, 3);
		g2.addEdge(0, 1);
		g2.addEdge(0, 2);
		g2.addEdge(0, 3);
		//identity instead of equality
		assertEquals(false, grafo == g2);	//false because it doesn't refer to the same graph
		assertEquals(true, grafo.equals(g2));	//true because the two graphs contain the same elements
	}

	@Test
	void testGetBFSTree() {
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addEdge(0, 1);
		grafo.addEdge(0, 2);
		grafo.addEdge(1, 2);
		grafo.addEdge(1, 5);
		grafo.addEdge(2, 4);
		grafo.addEdge(3, 2);
		grafo.addEdge(3, 7);
		grafo.addEdge(4, 5);
		grafo.addEdge(4, 7);
		grafo.addEdge(5, 6);
		grafo.addEdge(7, 6);
		grafo.getBFSTree(0);
	}

	@Test
	void testGetDFSTree() {
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addEdge(0, 1);
		grafo.addEdge(0, 2);
		grafo.addEdge(1, 2);
		grafo.addEdge(1, 5);
		grafo.addEdge(2, 4);
		grafo.addEdge(3, 2);
		grafo.addEdge(3, 7);
		grafo.addEdge(4, 5);
		grafo.addEdge(4, 7);
		grafo.addEdge(5, 6);
		grafo.addEdge(7, 6);
		grafo.getDFSTree(0);
	}
	
	@Test
	void testGetEdgeWeight() {
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addEdge(0, 1);
		grafo.addEdge(0, 2);
		grafo.addEdge(1, 2);
		grafo.setEdgeWeight(0, 1, 5);
		assertEquals(5, grafo.getEdgeWeight(1,0));
	}
	
	@Test
	void testSetEdgeWeight() {
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addEdge(0, 1);
		grafo.addEdge(0, 2);
		grafo.addEdge(1, 2);
		grafo.setEdgeWeight(0, 1, 5);
		grafo.setEdgeWeight(0, 2, 4);
		assertEquals(5, grafo.getEdgeWeight(1,0));
		assertEquals(4, grafo.getEdgeWeight(0, 2));
	}
	
	@Test
	void testPrintFEList() {
		grafo.addVertex();
		grafo.addVertex();
		grafo.addVertex();
		grafo.addEdge(0, 1);
		grafo.addEdge(0, 2);
		grafo.addEdge(1, 2);
		grafo.setEdgeWeight(0, 1, 5);
		grafo.setEdgeWeight(0, 2, 4);
		grafo.setEdgeWeight(1, 2, 16);
		
		grafo.printFEList();
		
	}
}
