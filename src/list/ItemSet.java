package list;

import java.util.ArrayList;
import java.util.List;

public class ItemSet extends ArrayList<Integer> implements Comparable<ItemSet> {
    int supportCount;

    public ItemSet() {
    }

    public ItemSet(int supportCount) {
        this.supportCount = supportCount;
    }

    public Integer get(int entry) {
        return super.get(entry);
    }

    public boolean add(int value) {
        if (super.contains(value)) {
            return false;
        }
        else {
            super.add(value);
            return true;
        }
    }

    public boolean add(Integer value) {
        if (super.contains(value)) {
            return false;
        }
        else {
            super.add(value);
            return true;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this == null) ? 0 : super.hashCode());
        return result;
    }

    @Override
    public int compareTo(ItemSet that) {
        List<Integer> thisItems = this;
        List<Integer> thatItems = that;
        if (thisItems == thatItems) {
            return 0;
        }

        for (int i = 0; i < thisItems.size(); i++) {
            int diff = thisItems.get(i).compareTo(thatItems.get(i));
            if (diff != 0) {
                return diff;
            }
        }
        return 0;
    }
}
