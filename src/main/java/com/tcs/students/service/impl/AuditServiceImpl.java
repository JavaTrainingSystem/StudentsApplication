package com.tcs.students.service.impl;

import com.tcs.students.dao.AuditRepo;
import com.tcs.students.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepo auditRepo;


    @Override
    public List<Map<String, String>> getAuditData(String featureType) {
        List<Object[]> result = auditRepo.findAuditDataWithUserName(featureType); // or use findAuditDataWithUserNameNative()

        return result.stream().map(row -> {
            Map<String, String> map = new HashMap<>();
            map.put("featureName", (String) row[0]);
            map.put("auditType", (String) row[1]);
            map.put("auditDate", row[2].toString());  // Convert Date to String if needed
            map.put("detailsInfo", (String) row[3]);
            map.put("userName", (String) row[4]);
            return map;
        }).collect(Collectors.toList());
    }
}
