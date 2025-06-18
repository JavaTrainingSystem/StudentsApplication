package com.tcs.students.dao;

import com.tcs.students.entity.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuditRepo extends JpaRepository<AuditEntity, Integer> {

    @Query("SELECT a.featureName, a.auditType, a.auditDate, a.detailsInfo, u.userName " +
            "FROM AuditEntity a " +
            "JOIN UserEntity u ON a.userId = u.userId " +
            "WHERE a.featureName = :featureType " +
            "ORDER BY a.auditDate DESC")
    List<Object[]> findAuditDataWithUserName(@Param("featureType") String featureType);

}
