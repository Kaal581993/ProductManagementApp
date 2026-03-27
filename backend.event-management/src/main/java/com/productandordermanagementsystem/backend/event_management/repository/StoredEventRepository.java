package com.productandordermanagementsystem.backend.event_management.repository;

import com.productandordermanagementsystem.backend.event_management.entity.StoredEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoredEventRepository extends JpaRepository<StoredEvent, Long> {
}
