package com.tcs.students.service;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface AuditService {

    List<Map<String, String>> getAuditData(@RequestParam String featureType);

}
