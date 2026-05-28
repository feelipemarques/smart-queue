package com.queue.smart_queue.ticket;

import com.queue.smart_queue.counter.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketResponse issueTicket(TicketPrefix prefix){
        Ticket ticket = new Ticket();
        ticket.setPrefix(prefix);
        ticketRepository.save(ticket);

        return new TicketResponse(formatTicket(prefix, ticket.getId()),ticket.getIssuedAt());
    }

    public List<TicketResponse> getOpenTickets(){
        return ticketRepository.findByStatus(TicketStatus.WAITING).stream()
                .map(ticket -> new TicketResponse(
                        formatTicket(ticket.getPrefix(), ticket.getId()), ticket.getIssuedAt()))
                .toList();
    }

    public void updateStatus(String number, TicketStatus status, Counter counter){
        Long id = Long.valueOf(number.substring(2));
        System.out.println("=============== Updating ticket status for id: " + id);
        if(ticketRepository.findById(id).isEmpty()) throw new RuntimeException("Ticket not found");

        Ticket ticket = ticketRepository.findById(id).get();
        if (!ticket.getCounter().equals(counter)) throw new RuntimeException("You can't finish another counter's ticket!");
        ticket.setStatus(status);
        ticketRepository.save(ticket);
    }

    public TicketCalledResponse callNext(Counter counter){
        Ticket nextTicket = ticketRepository.findAllWaitingOrdered(LocalDateTime.now().minusMinutes(30)).getFirst();

        if(ticketRepository.existsByCounterAndStatus(counter, TicketStatus.IN_SERVICE)){
            throw new RuntimeException("You must finish your current ticket first!");
        }

        nextTicket.setStatus(TicketStatus.IN_SERVICE);
        nextTicket.setCalledAt(LocalDateTime.now());
        nextTicket.setCounter(counter);
        ticketRepository.save(nextTicket);

        return new TicketCalledResponse(formatTicket(nextTicket.getPrefix(), nextTicket.getId()), nextTicket.getIssuedAt(), counter.getId());
    }

    private String formatTicket(TicketPrefix prefix, Long id){
        String add = "";
        if(id <= 9) add = "00";
        else if(id <= 99) add = "0";
        return prefix + add + id.toString();
    }
}
