package apriori;

import list.Transaction;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import utils.AprioriUtils;

import java.io.IOException;

/*
  Mapper for Phase1 would emit a <Individual Item, 1> pair for each item across all transactions.

  It read the dataset line by line and emit <Individual item, 1>

  For example,
  sample.txt
  +---------------+
  |  1 3 4 2 5  |  -> line number : 0 Items : 1 3 4 2 5
  |  2 3 5      |  -> line number : 1 Items : 2 3 5
  |  1 2 3 5    |  -> line number : 2 Items : 1 2 3 5
  |  2 5        |  -> line number : 3 Items : 2 5
  + --------------+

  In this class, The Key is line number, txnRecord is items.
  And you should emit in this form: [individual item] 1
  i.e [1] 1
      [3] 1
      [4] 1

*/

public class AprioriPass1Mapper extends Mapper<LongWritable, Text, Text, IntWritable>
{
    final static IntWritable one = new IntWritable(1);
    Text item = new Text();

    @Override
    public void map(LongWritable key, Text txnRecord, Context context)
            throws IOException, InterruptedException {
        Transaction txn = AprioriUtils.getTransaction((int) key.get(), txnRecord.toString());
        /** COMPLETE **/
        for(Integer val : txn) { // Go by each element in a transaction
        	item.set("[" + val + "]"); 
        	context.write(item, one); // Emit encountered values with one
        }
    }
}
