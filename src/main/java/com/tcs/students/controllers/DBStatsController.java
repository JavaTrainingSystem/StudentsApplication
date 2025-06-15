package com.tcs.students.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("api/db")
public class DBStatsController {


    @GetMapping("stats")
    public ResponseEntity<Map> aaaa(@RequestParam String type) {
        List<Map<String, Object>> chartData = new ArrayList<>();

        chartData.add(createChartEntry("portabull_generic", 442368, 23));
        chartData.add(createChartEntry("admin_hrms", 425984, 10));
        chartData.add(createChartEntry("user_management", 286720, 9));
        chartData.add(createChartEntry("dms", 90112, 6));
        chartData.add(createChartEntry("mis", 57344, 4));
        chartData.add(createChartEntry("spf", 40960, 3));

        // tableData as List of List (similar to 2D array)
        List<List<Object>> tableData = new ArrayList<>();

        tableData.add(Arrays.asList("Schema Name", "Table Count", "Schema Size"));
        tableData.add(Arrays.asList("portabull_generic", 23, 442368));
        tableData.add(Arrays.asList("admin_hrms", 10, 425984));
        tableData.add(Arrays.asList("user_management", 9, 286720));
        tableData.add(Arrays.asList("dms", 6, 90112));
        tableData.add(Arrays.asList("mis", 4, 57344));
        tableData.add(Arrays.asList("spf", 3, 40960));

        // Final Map to hold everything
        Map<String, Object> finalData = new HashMap<>();
        finalData.put("chartData", chartData);
        finalData.put("tableData", tableData);
        finalData.put("totalDBStorage", "11 MB");

        return new ResponseEntity<>(finalData, HttpStatus.OK);
    }

    private static Map<String, Object> createChartEntry(String name, int size, int count) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("name", name);
        entry.put("size", size);
        entry.put("count", count);
        return entry;
    }


    @GetMapping("monitor/dashboard")
    public ResponseEntity<Map> aasaa() {
        // Server metrics
        Map<String, List<Integer>> server = new HashMap<>();
        server.put("total", Arrays.asList(20, 20, 20, 20, 20, 20, 20, 20, 20, 20));
        server.put("idle", Arrays.asList(14, 14, 14, 14, 14, 14, 14, 14, 14, 14));
        server.put("active", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1));

        // Block I/O
        Map<String, List<Integer>> blockIO = new HashMap<>();
        blockIO.put("hits", Arrays.asList(
                11934970, 11934976, 11934982, 11934988, 11934994,
                11935009, 11935015, 11935137, 11935143, 11935149
        ));
        blockIO.put("read", Arrays.asList(828, 828, 828, 828, 828, 828, 828, 828, 828, 828));

        // Transactions
        Map<String, List<Integer>> transactions = new HashMap<>();
        transactions.put("rollback", Arrays.asList(14, 14, 14, 14, 14, 14, 14, 14, 14, 14));
        transactions.put("total", Arrays.asList(
                3089084, 3089094, 3089104, 3089114, 3089124,
                3089142, 3089152, 3089164, 3089174, 3089184
        ));
        transactions.put("commit", Arrays.asList(
                3089070, 3089080, 3089090, 3089100, 3089110,
                3089128, 3089138, 3089150, 3089160, 3089170
        ));

        // Timestamps
        List<String> timestamps = Arrays.asList(
                "17:58:40", "17:58:45", "17:58:50", "17:58:55", "17:59:00",
                "17:59:05", "17:59:10", "17:59:15", "17:59:20", "17:59:25"
        );

        // Final structure
        Map<String, Object> finalData = new HashMap<>();
        finalData.put("server", server);
        finalData.put("blockIO", blockIO);
        finalData.put("transactions", transactions);
        finalData.put("timestamps", timestamps);


        return new ResponseEntity<>(finalData, HttpStatus.OK);
    }


}
