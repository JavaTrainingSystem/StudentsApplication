package com.tcs.students.dao;

import com.tcs.students.entity.StudentEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepo extends JpaRepository<StudentEntity, Integer> {


    @Query("SELECT s FROM StudentEntity s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<StudentEntity> searchStudents(@Param(value = "name") String name, PageRequest pageRequest);


}
