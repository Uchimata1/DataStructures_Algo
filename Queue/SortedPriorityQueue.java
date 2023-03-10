package Queue;

import java.util.*;

import Queue.UnsortedPriorityQueue.Entry;
public class SortedPriorityQueue<K,V>{
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
	public SortedPriorityQueue(Comparator<K> c){comp=c;}
	public SortedPriorityQueue(){this(new DefaultComparator<K>());}
	//public boolean isEmpty(){return size==0;}
	private DoublyLinkedList<Entry<K,V>> list = new DoublyLinkedList<>();
	
	public Entry<K,V> findMin() {
		return list.head.next.element; 
	}
	
	public Entry<K,V> insert(K key, V value) {
		Entry<K,V> newest = new Entry<>(key,value);
		DoublyLinkedList.Node<Entry<K,V>> walk = list.tail.previous;
		while(walk.previous != null && comp.compare(newest.k,walk.element.k)<0) {
			walk = walk.previous;
		}
		list.insert(newest, walk, walk.next);
		return newest;
	}
	
	public Entry<K,V> removeMin(){
		if(list.isEmpty()) return null; 
		return list.delete(list.head.next); 
	}
}
