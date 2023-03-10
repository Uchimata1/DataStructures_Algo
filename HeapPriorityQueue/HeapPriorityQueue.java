package HeapPriorityQueue;

import java.util.ArrayList;
import java.util.Comparator;

public class HeapPriorityQueue<K,V>{
	protected ArrayList<Entry<K,V>> heap = new ArrayList<>();
	protected Comparator<K> comp;
	public HeapPriorityQueue() {this(new DefaultComparator<K>());}
	public HeapPriorityQueue(Comparator<K> c) {comp=c;}
	protected int leftchild(int j) {return 2*j+1;}
	protected int rightchild(int j) {return 2*j+2;}
	protected int parent(int j) {return (j-1)/2;}
	protected boolean hasLeft(int j) {return leftchild(j)<heap.size();}
	protected boolean hasRight(int j) {return rightchild(j)<heap.size();}
	
	protected void swap(int i, int j) {
		Entry<K,V> temp = heap.get(i); 
		heap.set(i, heap.get(j)); 
		heap.set(j, temp); 
	}
	
	public Entry<K,V> insert(K key, V value) {
		Entry<K,V> newest = new Entry<K,V>(key, value); 
		heap.add(newest); 
		swim(heap.size()-1); 
		return newest; 
	}
	
	protected void swim(int j){
		while(j>0) {
			if ( comp.compare(heap.get(j).key, heap.get(parent(j)).key) < 0) {
				swap(j,parent(j));
				j = parent(j);
			}
			else break;
		}
	}
	
	public Entry<K,V> removeMin() {
		if (heap.size()==0) return null;
		Entry<K,V> min = heap.get(0); 
		heap.set(0, heap.get(heap.size()-1)); 
		heap.remove(heap.size()-1);
		sink(0); 
		return min; 
	}
	
	public void sink(int j) {
		while (hasLeft(j)) { // stop condition: when current entry is already at bottom
			int minchild = leftchild(j); // get the minchild
			if (hasRight(j)) {
				if (comp.compare(heap.get(leftchild(j)).key,heap.get(rightchild(j)).key)> 0) {
					minchild = rightchild(j);
				}
			}
			// compare current node and its minchild
			if(comp.compare(heap.get(j).key, heap.get(minchild).key) > 0) {
				swap(j,minchild);
				j = minchild;
			}
			else break;
		}
	}

}
