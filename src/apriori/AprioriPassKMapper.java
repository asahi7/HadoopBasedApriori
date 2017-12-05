package apriori;

import list.ItemSet;
import list.Transaction;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import utils.AprioriUtils;
import trie.Trie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
*   Mapper for PhaseK would emit a <Frequent_ItemSet , 1> pair for each item across all transactions.
*/

public class AprioriPassKMapper extends Mapper<LongWritable, Text, Text, IntWritable>
{
    final static IntWritable one = new IntWritable(1);
    Text item = new Text();
    List<ItemSet> itemSetsPrevPass = new ArrayList<>();
    List<ItemSet> candidateItemSets = null;
    Trie trie = null;

    //In Pre-Map phase: (in setup() function that is invoked for each map instance)
    //  1) Self-join L_k to create itemsets of size k+1.
    //  2) For each subset of the above itemsets, we check if it is in L_k; we do this check approximately by keeping hashcode of itemsets in L_k in a set and check against this set.
    //     If all of the subsets are in L_k, then we add the itemset to C_(k+1).
    //  3) Add the itemsets in C_(k+1) into the Trie (implemented in the previous assignment).

    @Override
    public void setup(Context context)
            throws IOException {
        /** COMPLETE **/ // Ignore this Complete

        int passNum = context.getConfiguration().getInt("passNum", 2);      // getInt(String name, int defaultValue) : Get the value of the name property as an int
        String lastPassOutputFile = "output" + (passNum - 1) + "/part-r-00000";	

        // In try part, it reads the itemSet from the previous pass.
        try {
            Path path = new Path(lastPassOutputFile);
            FileSystem fs = FileSystem.get(context.getConfiguration());
            BufferedReader fis = new BufferedReader(new InputStreamReader(fs.open(path)));
            String currLine;

            // Each line is shown in the following form : [frequent ItemSet] support
            // i.e [1, 2] 3
            //     [2, 3] 2
            // Therefore, We need to filter '[' , ']'

            while ((currLine = fis.readLine()) != null) {
                currLine = currLine.replace("[", "");
                currLine = currLine.replace("]", "");
                currLine = currLine.trim();
                String[] words = currLine.split("[\\s\\t]+");
                if (words.length < 2) {
                    continue;
                }

                String finalWord = words[words.length - 1];
                int support = Integer.parseInt(finalWord);
                ItemSet itemSet = new ItemSet(support);

                for (int k = 0; k < words.length - 1; k++) {
                    String csvItemIds = words[k];
                    String[] itemIds = csvItemIds.split(",");
                    for (String itemId : itemIds) {
                        itemSet.add(Integer.parseInt(itemId));
                    }
                }
                itemSetsPrevPass.add(itemSet);
            }
        }
        catch (Exception e) {

        }
        // Generate the candidateItemSets using Self-Joining and pruning.
        candidateItemSets = AprioriUtils.getCandidateItemSets(itemSetsPrevPass, (passNum - 1));

        /** COMPLETE **/ // Ignore this Complete
        trie = new Trie(passNum);

        int candidateItemSetsSize = candidateItemSets.size();
        for (int i = 0; i < candidateItemSetsSize; i++) {
            ItemSet itemSet = candidateItemSets.get(i);
            trie.add(itemSet);
        }
    }

//  In n-Map phase:     (n > 1)
//    1) Read part of transaction file and for each transaction, call findItemsets() function to get the itemsets appearing in the transaction.
//    2) Emit the above itemsets as (itemset, 1).

    public void map(LongWritable key, Text txnRecord, Context context)
            throws IOException, InterruptedException {
        Transaction txn = AprioriUtils.getTransaction((int) key.get(), txnRecord.toString());
        /** COMPLETE **/
        Collections.sort(txn); // Sort the transactions list 
        ArrayList<ItemSet> matchedItemSet = new ArrayList<>();
        trie.findItemSets(matchedItemSet, txn); // Find all the candidates matching with itemsets
        										// formed from subsets from a transaction
        
        for(ItemSet itemSet : matchedItemSet) { // For each match emit it with value 1
        	item.set(itemSet.toString()); 
        	context.write(item, one);
        }
    }
}
