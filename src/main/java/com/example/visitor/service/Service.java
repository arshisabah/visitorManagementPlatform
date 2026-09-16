package com.example.visitor.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.example.visitor.entity.Visitor;
import com.example.visitor.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service {

    private static final Set<String> VALID_STATUSES = Set.of("pending", "approved", "rejected");

    private final com.example.visitor.repository.Repository repository;

    //get all visitors
    public List<Visitor> getAllVisitors() {
        return repository.findAll();
    }

    //create visitor with status as pending
    public Visitor createVisitor(Visitor visitor) {
        if (visitor.getName() == null || visitor.getName().isBlank()) {
            throw new IllegalArgumentException("Visitor name is required");
        }
        if (visitor.getReportingManager() == null || visitor.getReportingManager().isBlank()) {
            throw new IllegalArgumentException("reportingManager is required");
        }
        // status is always server-controlled on creation - never trust client input here
        visitor.setStatus("pending");
        visitor.setCheckInTime(null);
        visitor.setCheckOutTime(null);
        return repository.save(visitor);
    }

    //get pending visitors for a manager (filtered in the DB, not in memory)
    public List<Visitor> getPendingVisitorsForManager(String reportingManager) {
        return repository.findByReportingManagerIgnoreCaseAndStatusIgnoreCase(reportingManager, "pending");
    }

    //manager can approve or reject the visitor
    public Visitor updateVisitorStatus(Long id, String status) {
        if (status == null || !VALID_STATUSES.contains(status.toLowerCase())) {
            throw new IllegalArgumentException("status must be one of " + VALID_STATUSES + " but was '" + status + "'");
        }
        Visitor visitor = findVisitorOrThrow(id);
        visitor.setStatus(status.toLowerCase());
        return repository.save(visitor);
    }

    //get all approved visitors
    public List<Visitor> getAllApprovedVisitors() {
        return repository.findByStatusIgnoreCase("approved");
    }

    //get all rejected visitors
    public List<Visitor> getAllRejectedVisitors() {
        return repository.findByStatusIgnoreCase("rejected");
    }

    //check in time for a visitor - always the server clock, never client-supplied,
    //since letting clients dictate the timestamp would let anyone forge attendance logs
    public Visitor checkInVisitor(Long id) {
        Visitor visitor = findVisitorOrThrow(id);
        visitor.setCheckInTime(LocalDateTime.now().toString());
        return repository.save(visitor);
    }

    //check out time for a visitor
    public Visitor checkOutVisitor(Long id) {
        Visitor visitor = findVisitorOrThrow(id);
        if (visitor.getCheckInTime() == null) {
            throw new IllegalStateException("Visitor has not checked in yet");
        }
        visitor.setCheckOutTime(LocalDateTime.now().toString());
        return repository.save(visitor);
    }

    //time spent by a visitor in the office
    public String getTimeSpent(Long id) {
        Visitor visitor = findVisitorOrThrow(id);
        String checkInTime = visitor.getCheckInTime();
        String checkOutTime = visitor.getCheckOutTime();
        if (checkInTime == null || checkOutTime == null) {
            throw new IllegalStateException("Visitor has not checked in or checked out");
        }
        LocalDateTime checkIn = LocalDateTime.parse(checkInTime);
        LocalDateTime checkOut = LocalDateTime.parse(checkOutTime);
        Duration duration = Duration.between(checkIn, checkOut);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return hours + " hours " + minutes + " minutes";
    }

    private Visitor findVisitorOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visitor not found with id " + id));
    }
}
