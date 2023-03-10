package Queue;

public class ArrayQueue<E> {
	private int f = 0;
	private int size = 0;
	private E[] data;
	private static final int CAPACITY = 1000;
	public ArrayQueue(){this(CAPACITY);}
	public ArrayQueue(int capacity){data = (E[]) new Object[capacity];}
	
	public int size(){return size;}
	public boolean isEmpty(){return(size==0);}
	public E first(){return data[f];}
	
	public E dequeue() {
		if (isEmpty()) {return null;}
		E answer = data[f]; 
		data[f] = null; 
		f = (f + 1) % data.length; 
		size--; 		
		return answer; 
	}
	
	public void enqueue(E e) {
		if(size() == data.length) {
			E[] temp = (E[]) new Object[data.length*2];
			int cur_front = f;
			for (int i = 0; i < data.length; i++) {
				temp[i] = data[cur_front % data.length];
				cur_front++; 
			}
			f=0;
			data = temp;
		}
		data[(f + size)%data.length] = e; 
		size++; 
	}
}
