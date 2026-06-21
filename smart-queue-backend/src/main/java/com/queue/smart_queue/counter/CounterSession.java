package com.queue.smart_queue.counter;

import com.queue.smart_queue.attendant.Attendant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CounterSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Attendant attendant;

    @ManyToOne
    private Counter counter;

    private LocalDateTime loggedInAt;
    private LocalDateTime loggedOutAt;
}
