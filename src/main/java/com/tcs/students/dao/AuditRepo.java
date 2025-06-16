package com.tcs.students.dao;

import com.tcs.students.entity.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepo extends JpaRepository<AuditEntity, Integer> {

}
