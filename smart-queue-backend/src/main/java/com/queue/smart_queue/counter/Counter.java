package com.queue.smart_queue.counter;

import com.queue.smart_queue.ticket.Ticket;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Counter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean online = Boolean.FALSE;

    @OneToMany(mappedBy = "counter", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Ticket> tickets;

}
