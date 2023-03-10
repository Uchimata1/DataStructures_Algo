package Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph<V,E> {
	class Vertex<V>{
		public V element;
		public Map<Vertex<V>,Edge<E>> outgoing, incoming;
		public DoublyLinkedList.Node<Vertex<V>> position;
		public Vertex(V element, boolean graphIsDirected){
			this.element = element;
			outgoing = new HashMap<>();
			if(graphIsDirected) incoming = new HashMap<>();
			else incoming = outgoing;
		}
	}
	
	class Edge<E> {
		public E element;
		public Vertex<V>[] endpoints;
		public DoublyLinkedList.Node<Edge<E>> position;
		
		public Edge(Vertex<V> u, Vertex<V> v, E element) {
			this.element = element;
			endpoints = new Vertex[]{u,v};
		}
	}
	
	private DoublyLinkedList<Vertex<V>> vertices = new DoublyLinkedList<>();
	private DoublyLinkedList<Edge<E>> edges = new DoublyLinkedList<>();
	private boolean isDirected;
	
	public Graph(boolean isDirected){this.isDirected = isDirected;} //constructor
	
	public int numVertices() {return vertices.size;}
	public int numEdges() {return edges.size;}
	public int inDegree(Vertex<V> v) {return v.incoming.size();}
	public int outDegree(Vertex<V> v){return v.outgoing.size();}
	
	public Edge<E> getEdge(Vertex<V> u, Vertex<V> v){
		return u.outgoing.get(v); 
	}
	
	// Given the edge e and one of its endpoints, return the other endpoint.
	public Vertex<V> opposite(Vertex<V> v, Edge<E> e) {
		if (e.endpoints[0] == v) return e.endpoints[1];
		if (e.endpoints[1] == v) return e.endpoints[0];
		else throw new IllegalArgumentException("v is not incident to this edge");
	}
	
	public Vertex<V> insertVertex(V element) {
		Vertex<V> v = new Vertex(element, isDirected); 
		v.position = vertices.addLast(v); 
		return v; 
	}
	
	public Edge<E> insertEdge(Vertex<V> u, Vertex<V> v, E element) {
		if (getEdge(u, v) == null) {
			Edge<E> e = new Edge<E>(u, v, element);
			e.position = edges.addLast(e);
			u.outgoing.put(v, e);
			v.incoming.put(u, e); 
			return e; 
		}
		else throw new IllegalArgumentException("edge already exist");
	}
	
	public void removeEdge(Edge<E> e) {
		Vertex<V> origin = e.endpoints[0]; 
		Vertex<V> destination = e.endpoints[1]; 
		origin.outgoing.remove(destination); 	
		destination.incoming.remove(origin); 
		edges.delete(e.position); 
	}
	
	public void removeVertex(Vertex<V> v) {
		for (Edge<E> e : v.outgoing.values()) {
			removeEdge(e); 
		}
		for (Edge<E> e : v.incoming.values()) {
			removeEdge(e); 
		}
			
		vertices.delete(v.position); 
	}
	
	public void DFS(Vertex<V> v, HashMap<Vertex<V>,Edge<E>> forest) { // O(n + m); n = vertices m = edges
		for (Vertex<V> u : v.outgoing.keySet()) {
			if (forest.get(u) == null) {
				forest.put(u, getEdge(v,u)); 
				DFS(u, forest); 
			}			
		}
	}
	
	public HashMap<Vertex<V>,Edge<E>> DFSWrapper(Vertex<V> v) {
		HashMap<Vertex<V>,Edge<E>> forest = new HashMap<>();
		forest.put(v,null);
		DFS(v,forest);
		return forest;
	}
	
	public HashMap<Vertex<V>,Edge<E>> BFS(Vertex<V> u) { // O(n + m); n = vertices m = edges
		HashMap<Vertex<V>,Edge<E>> record = new HashMap<>();
		record.put(u,null);
		ArrayList<Vertex<V>> curLevel = new ArrayList<>();
		curLevel.add(u); 
		while (!curLevel.isEmpty()) {
			ArrayList<Vertex<V>> newLevel = new ArrayList<>();			
			for (Vertex<V> x : curLevel) {
				for (Vertex<V> v : x.outgoing.keySet()) {
					if (!record.containsKey(v)) {
						record.put(v, x.outgoing.get(v)); 
						newLevel.add(v); 
					}
				}
			}
			curLevel = newLevel; 
		}
		return record; 
	}
	
	
	// don't understand
	public boolean CycleDetection(HashMap<Vertex<V>,Edge<E>> record, Vertex<V> u, HashMap<Vertex<V>,Edge<E>>ancestors){
		for (Edge<E> e: u.outgoing.values()){
			Vertex<V> v = opposite(u, e);
			if(ancestors.containsKey(v)) return true;
			if(record.containsKey(v)==false){
				record.put(v,e);
				ancestors.put(v,null);
				if (CycleDetection(record,v,ancestors)==true) return true;
				ancestors.remove(v);
			}
		}
		return false;
	}
	
	public HashMap<Vertex<V>,Edge<E>> CycleDetectionWrapper(Vertex<V> v){
		HashMap<Vertex<V>,Edge<E>> record = new HashMap<>();
		HashMap<Vertex<V>,Edge<E>> ancestors = new HashMap<>();
		record.put(v,null);
		ancestors.put(v,null);
		CycleDetection(record,v,ancestors);
		return record;
	}
	
	public ArrayList<Vertex<V>> topologicalSort(){
		ArrayList<Vertex<V>> ordering = new ArrayList<>();
		HashMap<Vertex<V>,Integer> inDegree = new HashMap<>();
		// Step 1: Find source initial vertices
		for (Vertex<V> v:vertices){
			inDegree.put(v,v.incoming.size());
			if(inDegree.get(v)==0){
				ordering.add(v);
			}
		}
		
		// Step 2: Prune DAG
		for(int i=0;i<ordering.size();i++){
			Vertex<V> curV = ordering.get(i);
			for (Vertex<V> v: curV.outgoing.keySet()){
				int newInDegree = inDegree.get(v)-1;
				inDegree.put(v,newInDegree);
				if(newInDegree == 0) ordering.add(v);
			}
		}
		return ordering;
	}
	 
}
