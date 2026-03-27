package com.productandordermanagementsystem.backend.event_management.service;

import com.productandordermanagementsystem.backend.event_management.entity.StoredEvent;
import com.productandordermanagementsystem.backend.event_management.repository.StoredEventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class EventConsumerService {

    private final StoredEventRepository repository;

    public EventConsumerService(StoredEventRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "${app.kafka.order-topic}")
    public void consume(String payload) {
        StoredEvent event = new StoredEvent();
        event.setEventKey("order.placed");
        event.setEventType("ORDER_PLACED");
        event.setPayload(payload);
        repository.save(event);
    }
}
