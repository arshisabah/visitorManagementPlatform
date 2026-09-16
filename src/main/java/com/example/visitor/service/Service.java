package com.example.visitor.service;

import java.util.List;
import com.example.visitor.entity.Visitor;
import lombok.RequiredArgsConstructor;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

    private final com.example.visitor.repository.Repository repository;

    //get all visitors
    public List<Visitor> getAllVisitors() {
        return repository.findAll();
    }

    //create visitor with status as pending
    public Visitor createVisitor(Visitor visitor) {
        visitor.setStatus("pending");
        return repository.save(visitor);
    }

    //get pending visitors for a manager
    public List<Visitor> getPendingVisitorsForManager(String reportingManager) {
        return repository.findAll().stream()
                .filter(visitor -> visitor.getReportingManager().equals(reportingManager) && visitor.getStatus().equals("pending"))
                .toList();
    }

    //manager can approve or reject the visitor
    public Visitor updateVisitorStatus(Long id, String status) {
        Visitor visitor = repository.findById(id).orElseThrow(() -> new RuntimeException("Visitor not found"));
        visitor.setStatus(status);
        return repository.save(visitor);
    }

    //get all approved visitors
    public List<Visitor> getAllApprovedVisitors() {
        return repository.findAll().stream()
                .filter(visitor -> visitor.getStatus().equals("approved"))
                .toList();
    }

    //get all rejected visitors
    public List<Visitor> getAllRejectedVisitors() {
        return repository.findAll().stream()
                .filter(visitor -> visitor.getStatus().equals("rejected"))
                .toList();
    }

    //check in time for a visitor
    public Visitor checkInVisitor(Long id, String checkInTime) {
        Visitor visitor = repository.findById(id).orElseThrow(() -> new RuntimeException("Visitor not found"));
        visitor.setCheckInTime(checkInTime);
        return repository.save(visitor);
    }

    //check out time for a visitor
    public Visitor checkOutVisitor(Long id, String checkOutTime) {
        Visitor visitor = repository.findById(id).orElseThrow(() -> new RuntimeException("Visitor not found"));
        visitor.setCheckOutTime(checkOutTime);
        return repository.save(visitor);
    }

    //time spent by a visitor in the office
    public String getTimeSpent(Long id) {
        Visitor visitor = repository.findById(id).orElseThrow(() -> new RuntimeException("Visitor not found"));
        String checkInTime = visitor.getCheckInTime();
        String checkOutTime = visitor.getCheckOutTime();
        if (checkInTime == null || checkOutTime == null) {
            throw new RuntimeException("Visitor has not checked in or checked out");
        }
        java.time.LocalDateTime checkIn = java.time.LocalDateTime.parse(checkInTime);
        java.time.LocalDateTime checkOut = java.time.LocalDateTime.parse(checkOutTime);
        java.time.Duration duration = java.time.Duration.between(checkIn, checkOut);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return hours + " hours " + minutes + " minutes";
    }
}
