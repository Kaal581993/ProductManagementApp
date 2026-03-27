package com.productandordermanagementsystem.backend.event_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "stored_events")
public class StoredEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String eventKey;
    @Column(nullable = false)
    private String eventType;
    @Column(nullable = false, length = 4000)
    private String payload;
    @Column(nullable = false)
    private LocalDateTime receivedAt;

    @PrePersist
    void onCreate() { receivedAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public String getEventKey() { return eventKey; }
    public void setEventKey(String eventKey) { this.eventKey = eventKey; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public LocalDateTime getReceivedAt() { return receivedAt; }
}
