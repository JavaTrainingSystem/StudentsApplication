package com.tcs.students.service;

import java.util.Map;
import java.util.Optional;

public interface DBStatsService {

    Map<String, Object> getDBStats(String type, Optional<String> schemaName, Optional<String> tableName);

    Map<String, Object> getDBDashboard();


}
