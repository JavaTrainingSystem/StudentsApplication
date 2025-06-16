package com.tcs.students.controllers;

import com.tcs.students.service.DBStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/db")
@RequiredArgsConstructor
public class DBStatsController {

    private final DBStatsService dbStatsService;

    @GetMapping("stats")
    public ResponseEntity<Map<String, Object>> getDBStats(@RequestParam String type, @RequestParam(required = false) Optional<String> schemaName,
                                                          @RequestParam(required = false) Optional<String> tableName) {
        return new ResponseEntity<>(dbStatsService.getDBStats(type, schemaName, tableName), HttpStatus.OK);
    }

    @GetMapping("monitor/dashboard")
    public ResponseEntity<Map<String, Object>> getDBDashboard() {
        return new ResponseEntity<>(dbStatsService.getDBDashboard(), HttpStatus.OK);
    }


}
