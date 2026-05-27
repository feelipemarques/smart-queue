package com.queue.smart_queue.counter;

import com.queue.smart_queue.ticket.Ticket;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Counter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean online;

    @OneToMany(mappedBy = "counter", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Ticket> tickets;

}
