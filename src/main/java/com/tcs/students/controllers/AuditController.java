package com.tcs.students.controllers;

import com.tcs.students.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/audit")
    public ResponseEntity<List<Map<String, String>>> getAuditData(@RequestParam String featureType) {
        return ResponseEntity.ok(auditService.getAuditData(featureType));
    }
}
