package main;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.neo4j.helpers.collection.MapUtil.map;

/**
 * @author tbaum
 * @since 15.03.2014
 */
public class FileDescriptorLeak {

    private final GraphDatabaseService gdb;
    private final ExecutionEngine cypher;
    private final List<String> cachedIds = new ArrayList<>();

    public FileDescriptorLeak() {
        gdb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder("target").newGraphDatabase();
        cypher = new ExecutionEngine(gdb);

        try (Transaction transaction = gdb.beginTx()) {
            cypher.execute("CREATE CONSTRAINT ON (i:User) ASSERT i.id IS UNIQUE");
            transaction.success();
        }
    }

    public static void main(String[] args) throws Exception {
        new FdMonitor().start();

        new FileDescriptorLeak().loop();
    }


    private void loop() {
        //noinspection InfiniteLoopStatement
        while (true) {
            try (Transaction transaction = gdb.beginTx()) {
                cypher.execute("MERGE (u1:User {id: {u1}})" +
                                "  ON CREATE SET u1.created=timestamp() ON MATCH SET u1.updated=timestamp() " +
                                "MERGE (u2:User {id: {u2}})" +
                                "  ON CREATE SET u2.created=timestamp() ON MATCH SET u2.updated=timestamp() " +
                                "MERGE (u1)-[:FRIEND_OF]->(u2)",
                        map("u1", randomId(), "u2", randomId())
                );
                transaction.success();
            }
        }
    }

    /**
     * generate a random id, max 100000, reuse some
     */
    private String randomId() {
        int rnd = (int) (Math.random() * 100000);
        if (rnd >= cachedIds.size()) {
            String s = UUID.randomUUID().toString();
            cachedIds.add(s);
            return s;
        }
        return cachedIds.get(rnd);
    }

}
