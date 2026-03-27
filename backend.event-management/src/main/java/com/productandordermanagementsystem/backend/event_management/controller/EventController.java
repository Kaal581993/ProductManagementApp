package com.productandordermanagementsystem.backend.event_management.controller;

import com.productandordermanagementsystem.backend.event_management.entity.StoredEvent;
import com.productandordermanagementsystem.backend.event_management.repository.StoredEventRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final StoredEventRepository repository;

    public EventController(StoredEventRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<StoredEvent>> getEvents() {
        return ResponseEntity.ok(repository.findAll());
    }
}
