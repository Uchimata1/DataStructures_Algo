package Queue;

import java.util.*;
public class UnsortedPriorityQueue<K,V>{
	protected static class Entry<K,V>{
		public K k;
		public V v;
		public Entry(K key, V value){
			k = key;
			v = value;
		}
	}
	//private int size = 0;
	private Comparator<K> comp;
	public UnsortedPriorityQueue(Comparator<K> c){comp=c;}
	public UnsortedPriorityQueue(){this(new DefaultComparator<K>());}
	private DoublyLinkedList<Entry<K,V>> list = new DoublyLinkedList<>();

	public DoublyLinkedList.Node<Entry<K,V>> findMin() {
		DoublyLinkedList.Node<Entry<K,V>> min = list.head; 
		DoublyLinkedList.Node<Entry<K,V>> walk = list.head; 
		while (walk.next != null) {
			if (comp.compare(walk.element.k, min.element.k) < 0) {
				min = walk; 
			}
			walk = walk.next; 
		}
		return min; 
	}
	
	public Entry<K,V> insert(K key, V value) {
		Entry<K,V> e = new Entry<K,V>(key, value); 
		list.addLast(e); 
		return e; 
	}
	
	public Entry<K,V> removeMin(){
		if(list.isEmpty()) return null; 
		return list.delete(findMin()); 
	}
	
	
}
