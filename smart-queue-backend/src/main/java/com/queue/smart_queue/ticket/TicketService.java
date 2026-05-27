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

    public List<TicketResponse> getTickets(){
        return ticketRepository.findByStatus(TicketStatus.WAITING).stream()
                .map(ticket -> new TicketResponse(
                        formatTicket(ticket.getPrefix(), ticket.getId()), ticket.getIssuedAt()))
                .toList();
    }

    private String formatTicket(TicketPrefix prefix, Long id){
        String add = "";
        if(id <= 9) add = "00";
        else if(id <= 99) add = "0";
        return prefix + add + id.toString();
    }

}
