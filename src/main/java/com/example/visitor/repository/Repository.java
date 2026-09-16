package com.example.visitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.visitor.entity.Visitor;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Visitor, Long> {
}
