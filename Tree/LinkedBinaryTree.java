package Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LinkedBinaryTree<K,V> {
    protected static class Node<E>{
        public E element;
        public Node<E> parent;
        public Node<E> left;
        public Node<E> right;
        public Node(E e, Node<E> above, Node<E> leftChild, Node<E> rightChild){
            element = e;
            parent = above;
            left = leftChild;
            right = rightChild;
        }
    }
    protected Comparator<K> comp;
    protected Node<Entry<K,V>> root = null;
    private int size = 0;
    public LinkedBinaryTree(){comp = new DefaultComparator<K>();}
    public int size(){return size;}
    public Node<Entry<K,V>> root() {return root;}
    public int numChildren(Node<Entry<K,V>> p){
        int count = 0;
        if(p.left != null)
        count++;
        if(p.right != null)
        count++;
        return count;
    }
    public boolean isInternal(Node<Entry<K,V>> p) { return numChildren(p) > 0; }
    public boolean isExternal(Node<Entry<K,V>> p) { return numChildren(p) == 0; }
    public boolean isRoot(Node<Entry<K,V>> p) { return p == root(); }
    public boolean isEmpty( ) { return size( ) == 0; }
    
    public Node<Entry<K,V>> addLeft(Node<Entry<K,V>> p, Entry<K,V> e){
       if(p.left != null)
            throw new IllegalArgumentException("p already has a left child");
        Node<Entry<K,V>> child = new Node<>(e,p,null,null);
        p.left = child;
        size++;
        return child;
    }
    
    public Node<Entry<K,V>> addRight(Node<Entry<K,V>> p, Entry<K,V> e){
        if(p.right != null)
             throw new IllegalArgumentException("p already has a right child");
         Node<Entry<K,V>> child = new Node<>(e,p,null,null);
         p.right = child;
         size++;
         return child;
     }
     
    public Node<Entry<K,V>> addRoot(Entry<K,V> e){
        if(!isEmpty()) throw new IllegalStateException("Tree is not empty");
        root = new Node<Entry<K,V>>(e,null,null,null);
        size = 1;
        return root;
    }
    
    public int depth(Node<Entry<K,V>> p){
        if (p.parent==null) return 0;
        return depth(p.parent)+1;
    }

    public List<Node<Entry<K,V>>> traversal(String typeTraversal){
        List<Node<Entry<K,V>>> record = new ArrayList<>();
        if(!isEmpty()){
            if (typeTraversal == "preorder") {preorderSubtree(root,record);}
            else if (typeTraversal == "postorder") {postorderSubtree(root,record);}
            else if (typeTraversal == "inorder") {inorderSubtree(root,record);}
            else {breathfirst(record);}
        }
        return record;
    }
    
    protected void breathfirst(List<Node<Entry<K,V>>> record){
    	if(!isEmpty()) {
        	LinkedListQueue<Node<Entry<K,V>>> queue = new LinkedListQueue<>();
        	queue.enqueue(root);
        	while(!queue.isEmpty()) {
        		Node<Entry<K,V>> current = queue.dequeue();
        		record.add(current);	
        		if (current.left != null) { queue.enqueue(current.left);}
        		if (current.right != null) {queue.enqueue(current.right);}
        	} 	
        }
    }

    protected void preorderSubtree(Node<Entry<K,V>> p, List<Node<Entry<K,V>>> record) {
        record.add(p);//visit root
        //go into left child's branch
        if(p.left != null) preorderSubtree(p.left,record);
        //go into right child's branch
        if(p.right != null) preorderSubtree(p.right,record);
    }
    protected void postorderSubtree(Node<Entry<K,V>> p, List<Node<Entry<K,V>>> record) {
        // leftsubtree
        if(p.left != null) postorderSubtree(p.left,record);
        // rightsubtree
        if(p.right != null) postorderSubtree(p.right, record);
        // root
        record.add(p);
    }
    protected void inorderSubtree(Node<Entry<K,V>> p, List<Node<Entry<K,V>>> record) {
        if(p.left != null) inorderSubtree(p.left,record);
        record.add(p);
        if(p.right != null) inorderSubtree(p.right, record);
    }
	
    public Entry<K,V> remove(Node<Entry<K,V>> p){
        // if two children
        if(numChildren(p)==2) throw new IllegalArgumentException("two children!");

        // operate on child
        // define a child, to hold the only one child, if no child, then it is null.
        Node<Entry<K,V>> child = p.left;
        if(p.left == null){child = p.right;}
        if(child != null) child.parent = p.parent;
        // operate on p's parent
        if(p.parent == null) root = child;
        else{
            // whether update parent.left or parent.right?
            if(p==p.parent.left){
                p.parent.left = child;
            }
            else{
                p.parent.right = child;
            }
        }
        
        p.parent = null;
        p.left = null;
        p.right = null;
        p.element = null;


        return null;		
	}	
	
	public int height(Node<Entry<K,V>> p) { //don't understand  O(n) 
		int h = 0;
		if(p.left != null) h = Math.max(h,1+height(p.left));
		if(p.right != null) h = Math.max(h,1+height(p.right));
		return h;
	}
	
	public Node<Entry<K,V>> treeSearch(Node<Entry<K,V>> p, K key){
		if(isExternal(p)) return p; // fail to find
		if(comp.compare(key,p.element.key)>0) {
			return treeSearch(p.right,key);
		}
		else if(comp.compare(key,p.element.key)<0) {
			return treeSearch(p.left,key);
		}
		else // comp.compare(key,p.element.key)==0
			return p;
	}
	
	public Node<Entry<K,V>> get(K key){ // O(h)
		Node<Entry<K,V>> p = treeSearch(root,key);
		if(isExternal(p)) return null;
		else return p;
	}
	
	public void insert(K key, V value) {
		Node<Entry<K,V>> p = treeSearch(root, key); // search for key position
		if (isExternal(p)) {
			Entry<K,V> entry = new Entry<>(key, value);  //if not found insert
			expandExternal(p, entry); 
		}
		else {
			p.element.value = value; // if found replace value
		}
		rebalance(p);
	}
	
	public void expandExternal(Node<Entry<K,V>> p, Entry<K,V> e){
		p.element = e;
		p.left = new Node<Entry<K,V>>(null,p,null,null);
		p.right = new Node<Entry<K,V>>(null,p,null,null);
	}
	
	private void delete01(Node<Entry<K,V>> p){
		Node<Entry<K,V>> leaf = (isExternal(p.left) ? p.left : p.right);
		remove(leaf);
		remove(p);
	}
	
	public void delete(K key){
		Node<Entry<K,V>> p = treeSearch(root,key);
		if(isInternal(p)) {
			if(isExternal(p.left) || isExternal(p.right)){
				delete01(p);
			}
			else{
				Node<Entry<K,V>> replacement = treeMax(p.left);
				p.element = replacement.element;
				delete01(replacement);
			}	
		}
		rebalance(p.parent);
	}
	
	public Node<Entry<K,V>> treeMax(Node<Entry<K,V>> p){
		if(isExternal(p.right)) return p;
		return treeMax(p.right);
	}
	
	public void rotate(Node<Entry<K,V>> p){
		Node<Entry<K,V>> parent = p.parent;
		Node<Entry<K,V>> grandparent = parent.parent;
		//Step 1: relink c and a
		if(grandparent==null){
			root = p;
			p.parent = null;
		}
		else relink(grandparent, p, grandparent.left == parent);
		
		// Step 2:
		if(parent.left==p){
			relink(parent,p.right,true);
			relink(p,parent,false);}
		else{
			relink(parent,p.left,false);
			relink(p,parent,true);
		}
	}
	
	private void relink(Node<Entry<K,V>> parent, Node<Entry<K,V>> child, boolean makeLeftChild){
		child.parent = parent;
		if(makeLeftChild) parent.left = child;
		else parent.right = child;
	}
	
	public Node<Entry<K,V>> restructure(Node<Entry<K,V>> x){
		// we assume node x has parent and
		//grandparent as otherwise it is trivial
		//to handle by wrapper.
		Node<Entry<K,V>> y = x.parent;
		Node<Entry<K,V>> z = y.parent;
		if((x==y.left) == (y==z.left)){
			rotate(y);
			return y;
		}
		else{
			rotate(x);
			rotate(x);
			return x;
		}
	}
	
	protected void rebalance(Node<Entry<K,V>> p) {
		while(p != null) {
			if(Math.abs(height(p.left)-height(p.right))>=2){
				p = restructure(tallerChildNode(tallerChildNode(p)));
			}
			p = p.parent;
		}
	}
	
	protected Node<Entry<K,V>> tallerChildNode(Node<Entry<K,V>> p) {
		if(height(p.left) > height(p.right)) return p.left;
		else if(height(p.left) < height(p.right)) return p.right;
		else {
			if (p.parent == null) return p.left;
			if(p == p.parent.left) return p.left;
			else return p.right;
		}
	}
	
	private void splay(Node<Entry<K,V>> p) {
		while(!isRoot(p)) {
			// Situation 3: two-node rotation
			if(p.parent.parent==null) {rotate(p);
				break;
			}
			// Situation 2: tri-node straight-line
			if((p.parent.left == p) == (p.parent.parent.left==p.parent)){
				rotate(p.parent);
				rotate(p);
			}
			else{// Situation 1: tri-node zig-zag
				rotate(p);
				rotate(p);
			}
		}
	}
	
	public void accessSplay(K key){
		Node<Entry<K,V>> p = treeSearch(root,key);
		// Situation 1
		if (!isExternal(p)){
			splay(p);
		}
		// Situation 2
		else{
			if (p.parent != null) splay(p.parent);
		}
	}
	
	public void insertSplay(K key, V value){
		Node<Entry<K,V>> p = treeSearch(root,key);
		if(isExternal(p)) {
			Entry<K,V> entry = new Entry<>(key,value);
			expandExternal(p,entry);
		}
		else {
			p.element.value = value;
		}
		splay(p); // Fill this line!
	}
	
	public void deleteSplay(K key){
		Node<Entry<K,V>> p = treeSearch(root,key);
		if(isExternal(p))
		return;
		if(isExternal(p.left) || isExternal(p.right)){
		delete01(p);
		}
		else{
		Node<Entry<K,V>> replacement = treeMax(p.left);
		p.element = replacement.element;
		delete01(replacement);
		}
		if (p.parent != null) splay(p.parent);
	}
}
