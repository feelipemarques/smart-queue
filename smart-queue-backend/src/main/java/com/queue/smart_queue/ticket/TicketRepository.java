package com.queue.smart_queue.ticket;

import com.queue.smart_queue.counter.Counter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByStatus(TicketStatus status);

    @Query("""
        SELECT t FROM Ticket t
        WHERE t.status = 'WAITING'
        ORDER BY
            CASE
                WHEN t.issuedAt <= :threshold THEN 0
                WHEN t.prefix = 'AE' then 1
                WHEN t.prefix = 'AP' then 2
                ELSE 3
            END ASC,
            t.issuedAt ASC
        """)
    List<Ticket> findAllWaitingOrdered(@Param("threshold")LocalDateTime threshold);

    boolean existsByCounterAndStatus(Counter counter, TicketStatus status);

}
