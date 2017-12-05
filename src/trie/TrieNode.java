package trie;

import list.ItemSet;

import java.util.HashMap;

public class TrieNode extends HashMap<Integer, TrieNode> {
    ItemSet itemSet;
    boolean isLeafNode;

    public TrieNode() {
        isLeafNode = false;
    }

    public void setLeafNode(boolean isLeafNode) {
        this.isLeafNode = isLeafNode;
    }

    public ItemSet getItemSet() {
        return itemSet;
    }

    public void add(ItemSet itemSet) {
        this.itemSet = itemSet;
    }

    public boolean isLeafNode() {
        return isLeafNode;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IsLeaf : ").append(Boolean.toString(isLeafNode)).append("\t");
        builder.append("MapKeys :").append(keySet().toString()).append("\t");
        if (itemSet == null) {
            builder.append("ItemSets : ").append("[]");
        } else {
            builder.append("ItemSets : ").append(itemSet.toString());
        }

        return builder.toString();
    }
}

