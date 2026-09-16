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

@RestController
@RequestMapping("/api/visitors")
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

    @PutMapping("/{id}/status/{status}")
    public Visitor updateVisitorStatus(@PathVariable Long id, @PathVariable String status) {
        return service.updateVisitorStatus(id, status);
    }

    @GetMapping("/pending/{reportingManager}")
    public List<Visitor> getPendingVisitorsForManager(@PathVariable String reportingManager) {
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

    // no time param needed - the server clock sets it, so a visitor can't be checked in/out at a forged time
    @PutMapping("/{id}/check-in")
    public Visitor checkInVisitor(@PathVariable Long id) {
        return service.checkInVisitor(id);
    }

    @PutMapping("/{id}/check-out")
    public Visitor checkOutVisitor(@PathVariable Long id) {
        return service.checkOutVisitor(id);
    }

    //time spend by visitor
    @GetMapping("/{id}/time-spent")
    public String getTimeSpent(@PathVariable Long id) {
        return service.getTimeSpent(id);
    }
}
