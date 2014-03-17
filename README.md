start:

  mvn clean compile exec:java -Dexec.mainClass=main.FileDescriptorLeak


output:

...
[INFO]
[INFO] --- exec-maven-plugin:1.2.1:java (default-cli) @ test ---
open files: 101
open files: 764
open files: 1948
open files: 3223
open files: 4629
open files: 5897
open files: 7193
[WARNING]
java.lang.reflect.InvocationTargetException
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:606)
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:297)
	at java.lang.Thread.run(Thread.java:744)
Caused by: org.neo4j.graphdb.TransactionFailureException: Unable to commit transaction
	at org.neo4j.kernel.TopLevelTransaction.close(TopLevelTransaction.java:140)
	at main.FileDescriptorLeak.run(FileDescriptorLeak.java:73)
	at main.FileDescriptorLeak.main(FileDescriptorLeak.java:42)
	... 6 more
Caused by: org.neo4j.graphdb.TransactionFailureException: commit threw exception
	at org.neo4j.kernel.impl.transaction.TxManager.commit(TxManager.java:502)
	at org.neo4j.kernel.impl.transaction.TxManager.commit(TxManager.java:403)
	at org.neo4j.kernel.impl.transaction.TransactionImpl.commit(TransactionImpl.java:123)
	at org.neo4j.kernel.TopLevelTransaction.close(TopLevelTransaction.java:124)
	... 8 more
Caused by: javax.transaction.xa.XAException
	at org.neo4j.kernel.impl.transaction.TransactionImpl.doCommit(TransactionImpl.java:552)
	at org.neo4j.kernel.impl.transaction.TxManager.commit(TxManager.java:464)
	... 11 more
Caused by: org.neo4j.kernel.impl.nioneo.store.UnderlyingStorageException: java.io.FileNotFoundException: /Users/tbaum/Projekte/neo4j-fd-leak/neo-test/schema/label/lucene/_3v9.cfs (Too many open files in system)
	at org.neo4j.kernel.impl.nioneo.xa.NeoStoreTransaction.updateLabelScanStore(NeoStoreTransaction.java:908)
	at org.neo4j.kernel.impl.nioneo.xa.NeoStoreTransaction.applyCommit(NeoStoreTransaction.java:812)
...