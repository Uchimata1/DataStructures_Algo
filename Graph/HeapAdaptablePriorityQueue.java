package Graph;

import java.util.Comparator;

public class HeapAdaptablePriorityQueue<K,V>  extends HeapPriorityQueue<K,V> {
	protected static class AdaptableEntry<K,V> extends Entry<K,V>{
		private int index;
		public AdaptableEntry(K key, V value, int j){
			super(key,value);
			index = j;
		}
		public int getIndex() {return index;}
		public void setIndex(int j) {index = j;}
	}
	
	public HeapAdaptablePriorityQueue(Comparator<K> c) {super(c);}
	public HeapAdaptablePriorityQueue(){super();}
	
	protected AdaptableEntry<K,V> validate(AdaptableEntry<K,V> entry) { //verify entry exist
		int j = entry.index;
		if (j >= heap.size() || j<0 || heap.get(j).key != entry.key || heap.get(j).value != entry.value) {
			throw new IllegalArgumentException("Invalid entry");
		}
		return entry;
	}
		
	public void bubble(int j){
		if (comp.compare(heap.get(j).key,heap.get(parent(j)).key)<0){
			swim(j);
		}
		else sink(j);
	}
	
	public void replaceKey(AdaptableEntry<K,V> entry, K key) {
		AdaptableEntry<K,V> locator = validate(entry);
		heap.get(locator.index).key = key; 
		bubble(locator.index); 
	}
	
	@Override
	protected void swap(int i, int j) {
		super.swap(i, j);
		((AdaptableEntry<K,V>) heap.get(i)).setIndex(j); 
		((AdaptableEntry<K,V>) heap.get(j)).setIndex(i); 
	}
	
	public void remove(AdaptableEntry<K,V> entry) {
		AdaptableEntry<K,V> locator = validate(entry);
		int j = locator.getIndex();
		swap(j,heap.size()-1);
		heap.remove(heap.size()-1); 
		bubble(j); 
	}
	
	public void replaceValue(AdaptableEntry<K,V> entry, V value) {
		AdaptableEntry<K,V> locator = validate(entry); 
		heap.get(locator.index).value = value; 
	}
	
	
	@Override
	public Entry<K, V> insert(K key, V value) {
		AdaptableEntry<K,V> newest = new AdaptableEntry<K,V>(key, value, heap.size()); 
		heap.add(newest); 
		swim(heap.size()-1); 
		return newest; 
	}
	
	public static void main(String[] args) {
		HeapAdaptablePriorityQueue<Integer,Integer> alpha = new HeapAdaptablePriorityQueue();
		alpha.insert(1, 50); 
		alpha.insert(2, 10); 
		alpha.insert(3, 60); 
		System.out.println(alpha.heap.get(0).value); 
	}
}

