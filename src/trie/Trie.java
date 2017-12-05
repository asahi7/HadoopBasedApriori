package trie;

import java.util.ArrayList;
import java.util.List;

import list.ItemSet;
import list.Transaction;

/**
 * Trie are used for efficiently searching for a pattern of items in a transaction in frequent
 * itemset mining algorithms. This represents the structure of a Trie.
 */

public class Trie
{
    TrieNode rootNode;
    final int height;

    public Trie(int height) {
        rootNode = new TrieNode();
        this.height = height;
    }

    public boolean add(ItemSet itemSet) {
        /** COMPLETE **/
    	TrieNode node = rootNode;
    	for(Integer integer : itemSet) {
    		if(node.containsKey(integer)) {
    			node = node.get(integer);
    		} else {
    			node.put(integer, new TrieNode());
    			node = node.get(integer);
    		}
    	}
    	if(node.isLeafNode() == false) {
    		node.setLeafNode(true);
    		node.add(itemSet);
    		return true;
    	}
    	return false;
    }

    public boolean contains(ItemSet itemSet) {
        /** COMPLETE **/
    	TrieNode node = rootNode;
    	for(Integer integer : itemSet) {
    		if(node.containsKey(integer)) {
    			node = node.get(integer);
    		} else {
    			return false;
    		}
    	}
    	return node.isLeafNode(); 
    }

    public TrieNode getRootNode() {
        return rootNode;
    }

    private void recurse(ArrayList<ItemSet> matchedItemSetList, 
            TrieNode trieRootNode, 
            Transaction transaction,
            int startIndex) {
    	if(trieRootNode.isLeafNode() == true) {
			matchedItemSetList.add(trieRootNode.getItemSet());
			return;
		}
    	for(int i = startIndex; i < transaction.size(); i++) { 
    		int val = transaction.get(i);
    		if(trieRootNode.containsKey(val)) {
    			recurse(matchedItemSetList, trieRootNode.get(val), transaction, i + 1);
    		}
    	}
    }
    
    public void findItemSets(ArrayList<ItemSet> matchedItemSet, Transaction transaction) {
        /** COMPLETE **/
    	recurse(matchedItemSet, rootNode, transaction, 0);
    }
}

