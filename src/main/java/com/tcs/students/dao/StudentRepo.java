package com.tcs.students.dao;

import com.tcs.students.dto.AuditChartResult;
import com.tcs.students.entity.StudentEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepo extends JpaRepository<StudentEntity, Integer> {


    @Query("SELECT s FROM StudentEntity s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<StudentEntity> searchStudents(@Param(value = "name") String name, PageRequest pageRequest);


    @Query(value = """
            SELECT json_build_object(
                'deleted', deleted_counts,
                'created', created_counts,
                'labels', labels
            ) AS json_result
            FROM (
                SELECT
                    array_agg(COALESCE(deleted_count, 0) ORDER BY audit_date) AS deleted_counts,
                    array_agg(COALESCE(created_count, 0) ORDER BY audit_date) AS created_counts,
                    array_agg(to_char(audit_date, 'Mon DD') ORDER BY audit_date) AS labels
                FROM (
                    SELECT
                        audit_date::date AS audit_date,
                        COUNT(*) FILTER (WHERE audit_type = 'DELETED') AS deleted_count,
                        COUNT(*) FILTER (WHERE audit_type = 'CREATED') AS created_count
                    FROM audits
                    WHERE audit_date >= NOW() - CAST(:interval AS INTERVAL)
                      AND feature_name = 'STUDENTS'
                    GROUP BY audit_date::date
                    ORDER BY audit_date::date
                ) AS daily_counts
            ) AS final_result
            """, nativeQuery = true)
    AuditChartResult getAuditChartData(@Param("interval") String interval);


}
