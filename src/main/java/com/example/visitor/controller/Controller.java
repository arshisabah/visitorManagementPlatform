package com.example.visitor.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import com.example.visitor.entity.Visitor;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping ("/api/visitors")
@RequiredArgsConstructor 
public class Controller {

    private final com.example.visitor.service.Service service;

    @GetMapping 
    public List<Visitor> getAllVisitors() {
        return service.getAllVisitors();
    }

    @PostMapping
    public Visitor createVisitor(@RequestBody Visitor visitor) {
        return service.createVisitor(visitor);
    }

    @PutMapping("/{id}/status")
    public Visitor updateVisitorStatus(@PathVariable Long id, @RequestParam String status) {
        return service.updateVisitorStatus(id, status);
    }

    @GetMapping("/pending")
    public List<Visitor> getPendingVisitorsForManager(@RequestParam String reportingManager) {
        return service.getPendingVisitorsForManager(reportingManager);
    }

    @GetMapping("/approved")
    public List<Visitor> getAllApprovedVisitors() {
        return service.getAllApprovedVisitors();
    }

    @GetMapping("/rejected")
    public List<Visitor> getAllRejectedVisitors() {
        return service.getAllRejectedVisitors();
    }

    @PutMapping("/{id}/check-in")
    public Visitor checkInVisitor(@PathVariable Long id, @RequestParam String checkInTime) {
        return service.checkInVisitor(id, checkInTime);
    }

    @PutMapping("/{id}/check-out")
    public Visitor checkOutVisitor(@PathVariable Long id, @RequestParam String checkOutTime) {
        return service.checkOutVisitor(id, checkOutTime);
    }

    //time spend by visitor
    @GetMapping("/{id}/time-spent")
    public String getTimeSpent(@PathVariable Long id) {
        return service.getTimeSpent(id);
    }
}
