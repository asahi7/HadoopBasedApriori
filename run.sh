rm -Rf classes/*
javac -classpath $($HADOOP_HOME/bin/hadoop classpath) -d classes ./src/apriori/AprioriDriver.java ./src/apriori/AprioriPass1Mapper.java ./src/apriori/AprioriPassKMapper.java ./src/apriori/AprioriReducer.java ./src/list/ItemSet.java ./src/list/Transaction.java ./src/trie/Trie.java ./src/trie/TrieNode.java ./src/utils/AprioriUtils.java
jar -cvf HadoopBasedApriori.jar -C classes .
