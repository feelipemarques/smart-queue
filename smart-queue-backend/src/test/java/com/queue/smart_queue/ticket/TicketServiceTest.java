package com.queue.smart_queue.ticket;

import com.queue.smart_queue.counter.Counter;
import com.queue.smart_queue.exception.CounterInServiceException;
import com.queue.smart_queue.exception.EmptyQueueException;
import com.queue.smart_queue.exception.TicketNotCalledException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    public void shouldIssueTicketAndFormatItCorrectly(){
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setPrefix(TicketPrefix.AE);

        when(ticketRepository.save(any())).thenReturn(ticket);

        TicketResponse response = ticketService.issueTicket(TicketPrefix.AE);
        assertEquals("AE001", response.number());
    }

    @Test
    public void shouldCallSpecialBeforePriority(){
        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        ticket1.setPrefix(TicketPrefix.AE);

        Ticket ticket2 = new Ticket();
        ticket2.setId(2L);
        ticket2.setPrefix(TicketPrefix.AP);

        Counter counter = new Counter();

        when(ticketRepository.findAllWaitingOrdered(any())).thenReturn(List.of(ticket1, ticket2));

        assertEquals("AE001", ticketService.callNext(counter).number());
    }

    @Test
    public void shouldThrowExceptionWhenCounterIsInService(){
        when(ticketRepository.findAllWaitingOrdered(any())).thenReturn(List.of(new Ticket()));
        when(ticketRepository.existsByCounterAndStatus(any(), eq(TicketStatus.IN_SERVICE))).thenReturn(true);

        assertThrows(CounterInServiceException.class, () -> ticketService.callNext(new Counter()));
    }

    @Test
    public void shouldThrowExceptionWhenQueueIsEmpty(){
        when(ticketRepository.findAllWaitingOrdered(any())).thenReturn(List.of());
        assertThrows(EmptyQueueException.class, () -> ticketService.callNext(new Counter()));
    }

    @Test
    public void shouldThrowExceptionWhenTicketHasNoCounter(){
        Ticket ticket = new Ticket();
        when(ticketRepository.findById(any())).thenReturn(Optional.of(ticket));
        assertThrows(TicketNotCalledException.class,
                () -> ticketService.updateStatus("AE001", TicketStatus.FINISHED, ticket.getCounter()));
    }


}
