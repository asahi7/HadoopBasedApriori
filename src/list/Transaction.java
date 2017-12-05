package list;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a real-world retail store transaction. It consists of :
 * 	- tid : Transaction Id
 *
 */

//
public class Transaction extends ArrayList<Integer>
{
	int tid;

	public Transaction(int tid) {
		this.tid = tid;
	}

	@Override
	public String toString() {
		return "Transaction [tid=" + tid + ",items=" + super.toString() + "]";
	}

	@Override
	public boolean add(Integer integer) {
        super.add(integer);
		Collections.sort(this);
		return true;
	}
}
