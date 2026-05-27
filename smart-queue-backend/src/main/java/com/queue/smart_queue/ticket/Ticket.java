package com.queue.smart_queue.ticket;

import com.queue.smart_queue.counter.Counter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TicketPrefix prefix;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.WAITING;

    private LocalDateTime issuedAt =  LocalDateTime.now();
    private LocalDateTime calledAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Counter counter;

}
