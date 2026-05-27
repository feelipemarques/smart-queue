package com.queue.smart_queue.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void updateStatus(String number, TicketStatus status){
        Long id = Long.valueOf(number.substring(1, number.length()-1));

        if(ticketRepository.findById(id).isEmpty()) throw new RuntimeException("Ticket not found");

        Ticket ticket = ticketRepository.findById(id).get();
        ticket.setStatus(status);
        ticketRepository.save(ticket);
    }

    public TicketResponse callNext(){
        return null; //Implementar
    }

    private String formatTicket(TicketPrefix prefix, Long id){
        String add = "";
        if(id <= 9) add = "00";
        else if(id <= 99) add = "0";
        return prefix + add + id.toString();
    }
}
