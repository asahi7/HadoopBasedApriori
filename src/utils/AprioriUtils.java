package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import list.ItemSet;
import list.Transaction;

/*
 * Contains utility methods for Apriori algorithm
 */
public class AprioriUtils
{

// Returns a transaction object for the input txn record.
    public static Transaction getTransaction(int id, String txnRecord) {
        String currLine = txnRecord.trim();
        String[] words = currLine.split(" ");
        Transaction transaction = new Transaction(id);

        for (int i = 0; i < words.length; i++) {
            transaction.add(Integer.parseInt(words[i].trim()));
        }

        return transaction;
    }


// Determines if an item with the specified frequency has minimum support or not.
    public static boolean hasMinSupport(double minSup, int numTxns, int itemCount) {
        /** COMPLETE **/
    	double support = itemCount * 1.0 / numTxns;
    	if(Double.compare(support, minSup) >= 0) return true;
    	return false;
   }


//    L_k : frequent itemSets of length k.
//    C_k : candidate frequent itemSets of length k.

//  1) Self-join L_k to create itemSets of size k+1.
//  2) For each subset of the above itemSets, we check if it is in L_k; we do this check approximately by keeping hashcode of itemsets in L_k in a set and check against this set.
//     If all of the subsets are in L_k, then we add the itemSet to C_(k+1).   (pruning)
//  3) Add the itemSets in C_(k+1) into the Trie (implemented in the previous assignment).

/*
  In self-joining step, you can only generate the (k+1)-itemSet with k-itemSets having the same itemSet up to (k-1) items
  For example,
      Case 1
        Suppose we have 2 itemSet that have length 3 (list[0..2])
          [1, 2, 3], [1, 2, 4]

        In this case, you should check that the itemSets are the same from index 0 to index 1 and generate 4-newItemSet and pruning.

      Case 2
        Suppose we have 2 itemSet the have length 3 (list[0..2])
          [1, 2, 3], [2, 3, 4]

       In this case, you should only check index 0 and break.
       you don't need to generate the (k+1) itemSet

       Think about why consider generating (k+1)-newItemSet only for the same thing up to (k-1) consecutively.
*/

    public static List<ItemSet> getCandidateItemSets(List<ItemSet> prevPassItemSets, int itemSetSize) {
        List<ItemSet> candidateItemSets = new ArrayList<>();
        Map<Integer, ItemSet> itemSetMap = generateItemSetMap(prevPassItemSets);
        Collections.sort(prevPassItemSets);
        int prevPassItemSetsSize = prevPassItemSets.size();

        /** COMPLETE **/
        List<Integer> hashCodes = new ArrayList<>(); // for case {@code itemSetSize == 1} ? 
        for(int i = 0; i < prevPassItemSetsSize; i++) { // generates hash codes for each itemset in the {@code candidateItemSets} except their last elements 
        	ItemSet subItemSet = new ItemSet();
        	subItemSet.addAll(prevPassItemSets.get(i).subList(0, itemSetSize - 1));
        	if(subItemSet.size() > 0) hashCodes.add(subItemSet.hashCode());
        	else hashCodes.add(0);
        }
        
        for(int i = 0; i < prevPassItemSetsSize; i++) { // for case {@code itemSetSize == 1} ? 
        	for(int j = i + 1; j < prevPassItemSetsSize; j++) {
        		if(hashCodes.get(i).equals(hashCodes.get(j))) {
        			ItemSet newItemSet = new ItemSet();
        			if(itemSetSize > 1) newItemSet.addAll(prevPassItemSets.get(i).subList(0, itemSetSize - 1));
        			newItemSet.add(prevPassItemSets.get(i).get(itemSetSize - 1));
        			newItemSet.add(prevPassItemSets.get(j).get(itemSetSize - 1));
        			candidateItemSets.add(newItemSet);
        		} else {
        			break;
        		}
        	}
        }
        
        for(Iterator<ItemSet> iterator = candidateItemSets.iterator(); iterator.hasNext();) {
        	ItemSet candidate = iterator.next();
        	if(prune(itemSetMap, candidate) == false) {
        		iterator.remove();
        	}
        }
        
        return candidateItemSets;
    }


// Generates a map of hashcode and the corresponding ItemSet. Since multiple entries can
// have the same hashcode, there would be a list of ItemSets for any hashcode.
// It is used to verify that the subset of C_(K+1) belongs to L_K during the pruning process.

    public static Map<Integer, ItemSet> generateItemSetMap(List<ItemSet> itemSets) {
        Map<Integer, ItemSet> itemSetMap = new HashMap<>();

        for (ItemSet itemSet : itemSets) {
            int hashCode = itemSet.hashCode();
            if (!itemSetMap.containsKey(hashCode)) {
                itemSetMap.put(hashCode, itemSet);
            }
        }
        return itemSetMap;
    }

// This method checks that the subset of C_(K+1) belongs to L_K.
// If all of the subsets are in L_K, then it return true, otherwise false.
// Refer to Lecture 9.

    static boolean prune(Map<Integer, ItemSet> itemSetsMap, ItemSet newItemSet) {
        List<ItemSet> subsets = getSubSets(newItemSet);

        for (ItemSet subItemSet : subsets) {
            int hashCodeToSearch = subItemSet.hashCode();
            if (!itemSetsMap.containsKey(hashCodeToSearch)) {
                return false;
            }
        }
        return true;
    }
    
    /**
	 * Finds all the subsets of length {@code itemSet.size() - 1} from the {@code itemSet}.
	 * Iterates through all elements of the list and if any element is put into newItemSet, 
	 * then goes further down onto recursive tree with new v pointing to the next of taken element.
     * @param v
     * @param itemSet
     * @param subSets
     * @param taken
     * @param newItemSet
     */
    static void findSubSets(int v, ItemSet itemSet, List<ItemSet> subSets, boolean[] taken, ItemSet newItemSet) {
    	if(newItemSet.size() == itemSet.size() - 1){ // Add to the {@code subSets} newly formed itemSet
    		ItemSet newItemSetCpy = new ItemSet();
    		newItemSetCpy.addAll(newItemSet);
    		subSets.add(newItemSetCpy);
    		return;
    	}
    	for(int i = v; i < itemSet.size(); i++){
    		if(taken[i] == false) { // If a value standing on index {@code i} is not taken, then take it
    			taken[i] = true;
    			newItemSet.add(itemSet.get(i));
    			findSubSets(i + 1, itemSet, subSets, taken, newItemSet); // Go down to the next recursive subtree
    			newItemSet.remove(newItemSet.size() - 1); // Remove previously inserted value
    			taken[i] = false;
    		}
    	}
    }
    
//  Generate all possible k-1 subsets for ItemSet (preserves order)
    static List<ItemSet> getSubSets(ItemSet itemSet) {
        List<ItemSet> subSets = new ArrayList<>();
        /** COMPLETE **/
        boolean taken[] = new boolean[itemSet.size()]; // element is {@code true} 
        											   // if the element standing on this index is taken to
        											   // the {@code newItemSet} list.
        for(int i = 0; i < itemSet.size(); i++) {
        	taken[i] = false;
        }
        ItemSet newItemSet = new ItemSet();
        if(itemSet.size() > 1) { // If the size of {@code itemSet} is greater than 1,
        						 // otherwise makes no sense.
        	findSubSets(0, itemSet, subSets, taken, newItemSet); 
        }
        return subSets;
    }
}
