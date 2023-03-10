package Trie;

public class Trie {
	// Alphabet size (# of symbols)
	static final int ALPHABET_SIZE = 26;
	// trie node
	static class TrieNode{
		TrieNode[] children = new TrieNode[ALPHABET_SIZE];
		TrieNode parent;
		char key;
		Integer position;

		TrieNode(TrieNode parent, char key){
			position = null;
			this.parent = parent;
			this.key = key;
			for (int i = 0; i < ALPHABET_SIZE; i++)
				children[i] = null;
		}
		
		int numChildren(){ 
            int count = 0;
            for (TrieNode e:children){
                if(e != null)
                    count++;
            }
            return count;
		}
	}
	static TrieNode root;
	
	
	static void insert(String query, Integer value){
		TrieNode node = root;
		for (int i = 0; i < query.length(); i++){
			int index = query.charAt(i) - 'a';
			if (node.children[index] == null)
				node.children[index] = new TrieNode(node,query.charAt(i));
			node = node.children[index];
		}
		node.position = value; // mark last node as leaf
	}
	
	static TrieNode search(String query){
		TrieNode node = root;
		for (int i = 0; i < query.length(); i++){
			int index = query.charAt(i) - 'a';
			if (node.children[index] == null) // doesn't exist 
				return null;
			node = node.children[index]; // update node
		}
		if(node.position == null)
			return null;
		else
			return node;
	}
	
	static boolean remove(String query) {
		TrieNode end = search(query);
		if (end == null) return false; // query not found
		if (end.numChildren()!=0){ // query found but it's an internal node
			end.position = null;
			return true;
		}
		TrieNode node = end;
		while(node.parent != null){
			node.parent.children[node.key-'a'] = null;
			if(node.parent.numChildren()>0 || node.parent.position != null)
				break;
			node = node.parent;
		}
		return true;
	}
	
	
}