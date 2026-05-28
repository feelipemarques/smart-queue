package com.queue.smart_queue.counter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CounterRepository extends JpaRepository<Counter, Long> {
    Optional<Counter> findByIdAndOnline(Long id, Boolean online);
}
