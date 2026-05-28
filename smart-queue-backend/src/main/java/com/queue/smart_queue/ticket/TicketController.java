package com.queue.smart_queue.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/tickets/new")
    public TicketResponse getTicket(@RequestParam("prefix") TicketPrefix prefix) {
        return ticketService.issueTicket(prefix);
    }

    @GetMapping("/tickets/queue")
    public List<TicketResponse> getOpenTickets(){
        return ticketService.getOpenTickets();
    }

}
