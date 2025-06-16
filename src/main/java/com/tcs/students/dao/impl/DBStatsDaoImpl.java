package com.tcs.students.dao.impl;

import com.tcs.students.dao.DBStatsDao;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TupleElement;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.postgresql.util.PGInterval;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class DBStatsDaoImpl implements DBStatsDao {

//    private final HibernateUtils hibernateUtils;

    private final SessionFactory sessionFactory;

    private final EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;

    private static final int MAX_POINTS = 10;

    private final LinkedList<String> timestamps = new LinkedList<>();
    private final Map<String, LinkedList<Number>> server = new HashMap<>();
    private final Map<String, LinkedList<Number>> transactions = new HashMap<>();
    private final Map<String, LinkedList<Number>> blockIO = new HashMap<>();

    private static final String TOTAL = "total";
    private static final String TABLE_DATA = "tableData";

    @PostConstruct
    public void init() {
        server.put("active", new LinkedList<>());
        server.put("idle", new LinkedList<>());
        server.put(TOTAL, new LinkedList<>());

        transactions.put("commit", new LinkedList<>());
        transactions.put("rollback", new LinkedList<>());
        transactions.put(TOTAL, new LinkedList<>());

        blockIO.put("read", new LinkedList<>());
        blockIO.put("hits", new LinkedList<>());
    }

    @Scheduled(fixedRate = 5000)
    public void collect() {
        String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        append(timestamps, timestamp);

        int active = query("SELECT COUNT(*) FROM pg_stat_activity WHERE state = 'active'");
        int idle = query("SELECT COUNT(*) FROM pg_stat_activity WHERE state = 'idle'");
        int total = query("SELECT COUNT(*) FROM pg_stat_activity");

        int commit = query("SELECT SUM(xact_commit) FROM pg_stat_database");
        int rollback = query("SELECT SUM(xact_rollback) FROM pg_stat_database");
        int txTotal = commit + rollback;

        int read = query("SELECT SUM(blks_read) FROM pg_stat_database");
        int hits = query("SELECT SUM(blks_hit) FROM pg_stat_database");

        append(server.get("active"), active);
        append(server.get("idle"), idle);
        append(server.get(TOTAL), total);

        append(transactions.get("commit"), commit);
        append(transactions.get("rollback"), rollback);
        append(transactions.get(TOTAL), txTotal);

        append(blockIO.get("read"), read);
        append(blockIO.get("hits"), hits);
    }

    private int query(String sql) {
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    private void append(LinkedList list, Object value) {
        if (list.size() >= MAX_POINTS) list.removeFirst();
        list.add(value);
    }

    private Map<String, List<Number>> deepCopy(Map<String, LinkedList<Number>> original) {
        Map<String, List<Number>> copy = new HashMap<>();
        for (Map.Entry<String, LinkedList<Number>> entry : original.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }


    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> result = new HashMap<>();
        result.put("timestamps", new ArrayList<>(timestamps));
        result.put("server", deepCopy(server));
        result.put("transactions", deepCopy(transactions));
        result.put("blockIO", deepCopy(blockIO));
        return result;
    }

    @Override
    public Map<String, Object> getDBStats(String type, Optional<String> schemaName, Optional<String> tableName) {
        Map<String, Object> response = new HashMap<>();
        try (Session session = sessionFactory.openSession()) {
            if (type.equalsIgnoreCase("schema")) {
                String totalDBStorage = session.createNativeQuery("SELECT pg_size_pretty(pg_database_size(current_database())) AS db_size", String.class).uniqueResult();
                response.put("totalDBStorage", totalDBStorage);

                List<Map<String, Object>> chartData = new ArrayList<>();

                List<Object[]> schemaStats = session.createNativeQuery("SELECT schemaname,COUNT(*) AS table_count,SUM(pg_total_relation_size(relid)) AS total_size FROM pg_catalog.pg_statio_user_tables GROUP BY schemaname ORDER BY SUM(pg_total_relation_size(relid)) DESC", Object[].class).list();

                List<Object[]> data = new ArrayList<>();

                Object[] columnNames = {"Schema Name", "Table Count", "Schema Size"};

                data.add(columnNames);

                for (Object[] obj : schemaStats) {
                    chartData.add(Map.of("name", obj[0], "count", obj[1], "size", obj[2]));
                    data.add(obj);
                }

                response.put(TABLE_DATA, data);
                response.put("chartData", chartData);
            } else if (type.equalsIgnoreCase("retrieveSchema") && schemaName.isPresent()) {
                String query = "SELECT  table_name, CASE WHEN pg_total_relation_size(quote_ident(table_schema) || '.' || quote_ident(table_name)) >= 1073741824 THEN ROUND(pg_total_relation_size(quote_ident(table_schema) || '.' || quote_ident(table_name)) / 1073741824.0, 2) || ' GB' WHEN pg_total_relation_size(quote_ident(table_schema) || '.' || quote_ident(table_name)) >= 1048576 THEN ROUND(pg_total_relation_size(quote_ident(table_schema) || '.' || quote_ident(table_name)) / 1048576.0, 2) || ' MB' WHEN pg_total_relation_size(quote_ident(table_schema) || '.' || quote_ident(table_name)) >= 1024 THEN ROUND(pg_total_relation_size(quote_ident(table_schema) || '.' || quote_ident(table_name)) / 1024.0, 2) || ' KB' ELSE '0 KB' END AS total_size FROM information_schema.tables WHERE table_type = 'BASE TABLE' AND table_schema = '" + schemaName.get() + "' ORDER BY pg_total_relation_size(quote_ident(table_schema) || '.' || quote_ident(table_name)) DESC";
                List<Object[]> schemaStats = session.createNativeQuery(query, Object[].class).list();


                List<Object[]> data = new ArrayList<>();

                Object[] columnNames = {"Table Name", "Table Size"};

                data.add(columnNames);

                for (Object[] obj : schemaStats) {
                    data.add(obj);
                }

                response.put(TABLE_DATA, data);
            } else if (type.equalsIgnoreCase("retrieveTable") && schemaName.isPresent() && tableName.isPresent()) {
                String query = "SELECT * from " + schemaName.get() + "." + tableName.get() + " limit 100";

                List<Object[]> data = new ArrayList<>();

                List<Tuple> results = entityManager.createNativeQuery(query, Tuple.class).getResultList();

                if (!CollectionUtils.isEmpty(results)) {
                    List<String> columnNames = results.get(0).getElements().stream()
                            .map(TupleElement::getAlias)
                            .toList();

                    data.add(columnNames.toArray());

                    for (Tuple tuple : results) {
                        Object[] row = new Object[columnNames.size()];
                        for (int i = 0; i < columnNames.size(); i++) {
                            row[i] = tuple.get(columnNames.get(i));
                        }
                        data.add(row);
                    }

                }

                response.put(TABLE_DATA, data);
            } else if (type.equalsIgnoreCase("currentConnections")) {
                String query = "SELECT datname AS database_name, usename AS user_name, client_addr, application_name, state, backend_start, query_start, wait_event_type, wait_event, query FROM pg_stat_activity WHERE state = 'active';";

                List<Object[]> data = new ArrayList<>();

                List<Tuple> results = entityManager.createNativeQuery(query, Tuple.class).getResultList();

                if (!CollectionUtils.isEmpty(results)) {
                    List<String> columnNames = results.get(0).getElements().stream()
                            .map(TupleElement::getAlias)
                            .toList();

                    data.add(columnNames.toArray());

                    for (Tuple tuple : results) {
                        Object[] row = new Object[columnNames.size()];
                        for (int i = 0; i < columnNames.size(); i++) {
                            row[i] = tuple.get(columnNames.get(i));
                        }
                        data.add(row);
                    }
                }

                response.put(TABLE_DATA, data);
            } else if (type.equalsIgnoreCase("longRunningQueries")) {
                String query = "SELECT pid, datname, usename, state, query, now() - query_start AS duration, query_start FROM pg_stat_activity WHERE state = 'active' AND now() - query_start > interval '30 seconds' ORDER BY duration DESC;";

                List<Object[]> data = new ArrayList<>();

                List<Tuple> results = entityManager.createNativeQuery(query, Tuple.class).getResultList();

                if (!CollectionUtils.isEmpty(results)) {
                    List<String> columnNames = results.get(0).getElements().stream()
                            .map(TupleElement::getAlias)
                            .toList();

                    data.add(columnNames.toArray());

                    for (Tuple tuple : results) {
                        Object[] row = new Object[columnNames.size()];
                        for (int i = 0; i < columnNames.size(); i++) {
                            Object obj = tuple.get(columnNames.get(i));
                            if (obj instanceof PGInterval interval) {
                                row[i] = interval.getSeconds() + " Seconds";
                            } else {
                                row[i] = obj;
                            }

                        }
                        data.add(row);
                    }
                }
                response.put(TABLE_DATA, data);
            }


        }
        return response;
    }

    public static String formatSize(long bytes) {
        double kb = bytes / 1024.0;

        if (kb < 1000) {
            return String.format("%.2f KB", kb);
        }

        double mb = kb / 1024.0;
        if (mb < 1000) {
            return String.format("%.2f MB", mb);
        }

        double gb = mb / 1024.0;
        if (gb < 1000) {
            return String.format("%.2f GB", gb);
        }

        double tb = gb / 1024.0;
        return String.format("%.2f TB", tb);
    }

}
