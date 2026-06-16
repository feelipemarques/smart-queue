package com.queue.smart_queue.ticket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    private Ticket anTicket;
    private Ticket apTicket;
    private Ticket ae1Ticket;
    private Ticket ae2Ticket;

    @BeforeEach
    public void setup(){
        anTicket = new Ticket();
        anTicket.setPrefix(TicketPrefix.AN);

        apTicket = new Ticket();
        apTicket.setPrefix(TicketPrefix.AP);

        ae1Ticket = new Ticket();
        ae1Ticket.setPrefix(TicketPrefix.AE);

        ae2Ticket = new Ticket();
        ae2Ticket.setPrefix(TicketPrefix.AE);

        ticketRepository.save(ae1Ticket);
        ticketRepository.save(apTicket);
        ticketRepository.save(anTicket);
        ticketRepository.save(ae2Ticket);
    }


    @Test
    public void shouldCallSpecialBeforeOthers(){
        assertEquals(ae1Ticket, ticketRepository.findAllWaitingOrdered(LocalDateTime.now().minusMinutes(30L)).getFirst());
    }

    @Test
    public void shouldCallPriorityBeforeNormal(){
        ticketRepository.delete(ae1Ticket);
        ticketRepository.delete(ae2Ticket);
        assertEquals(apTicket, ticketRepository.findAllWaitingOrdered(LocalDateTime.now().minusMinutes(30L)).getFirst());
    }

    @Test
    public void shouldCallNormalTicketWaitingMoreThanThirtyMinutesBeforeSpecial(){
        anTicket.setIssuedAt(LocalDateTime.now().minusMinutes(30L));
        ticketRepository.save(anTicket);
        assertEquals(anTicket, ticketRepository.findAllWaitingOrdered(LocalDateTime.now().minusMinutes(30L)).getFirst());
    }

    @Test
    public void shouldCallAe1BeforeAe2(){
        assertEquals(ae1Ticket, ticketRepository.findAllWaitingOrdered(LocalDateTime.now().minusMinutes(30L)).getFirst());
    }

}
