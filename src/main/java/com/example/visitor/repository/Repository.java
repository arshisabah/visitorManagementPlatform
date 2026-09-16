package com.example.visitor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.visitor.entity.Visitor;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Visitor, Long> {

    List<Visitor> findByReportingManagerIgnoreCaseAndStatusIgnoreCase(String reportingManager, String status);

    List<Visitor> findByStatusIgnoreCase(String status);
}
