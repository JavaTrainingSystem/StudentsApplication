package com.tcs.students.dao;

import java.util.Map;
import java.util.Optional;

public interface DBStatsDao {

    Map<String, Object> getDBStats(String type, Optional<String> schemaName, Optional<String> tableName);

    Map<String, Object> getStats();
}
