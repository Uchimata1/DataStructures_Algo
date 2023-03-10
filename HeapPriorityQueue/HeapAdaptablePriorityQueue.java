package HeapPriorityQueue;

import java.util.Comparator;

public class HeapAdaptablePriorityQueue<K,V> extends HeapPriorityQueue<K,V> {
	protected static class AdaptableEntry<K,V> extends Entry<K,V> {
		private int index;		
		public AdaptableEntry(K key, V value, int j){
			super(key,value);
			index = j;
		}
		
		public int getIndex(){return index;}
		public void setIndex(int j){index = j;}
	}
	
	public HeapAdaptablePriorityQueue(Comparator<K> c) {super(c);}
	public HeapAdaptablePriorityQueue(){super();}
	
	protected AdaptableEntry<K,V> validate(AdaptableEntry<K,V> entry) {
		int j = entry.index;
		if (j >= heap.size() || j<0 || heap.get(j).key != entry.key || heap.get(j).value != entry.value)
			throw new IllegalArgumentException("Invalid entry");
		return entry;
	}
	
	public void replaceKey(AdaptableEntry<K,V> entry, K key){
		AdaptableEntry<K,V> locator = validate(entry); 
		heap.get(locator.index).key = key; 
		bubble(locator.index); 
	}
	
	public void bubble(int j) {
		if (comp.compare(heap.get(j).key, heap.get(parent(j)).key ) < 0) {
			swim(j); 
		}
		else sink(j); 
	}
	
	public void remove(AdaptableEntry<K,V> entry) {
		AdaptableEntry<K,V> locator = validate(entry); 
		int j = locator.getIndex();
		swap(j,heap.size()-1);
		heap.remove(heap.size()-1);
		bubble(j); 
	}
	
	@Override
	protected void swap(int i, int j) {
		super.swap(i, j);
		((AdaptableEntry<K,V>) heap.get(i)).setIndex(j);  //why is it j and not i 
		((AdaptableEntry<K,V>) heap.get(j)).setIndex(i);
	}
	
	public void replaceValue(AdaptableEntry<K,V> entry, V value) {
		AdaptableEntry<K,V> locator = validate(entry); 
		heap.get(locator.index).value = value; 
	}
	
}
