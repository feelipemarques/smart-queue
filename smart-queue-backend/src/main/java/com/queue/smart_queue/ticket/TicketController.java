package com.queue.smart_queue.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/tickets")
    public TicketResponse getTicket(@RequestParam("prefix") TicketPrefix prefix) {
        return ticketService.issueTicket(prefix);
    }

    @GetMapping("/tickets")
    public List<TicketResponse> getOpenTickets(){
        return ticketService.getOpenTickets();
    }

    @GetMapping("/tickets/{ticket}")
    public TicketStatus getTicket(@PathVariable("ticket") String ticket){
        return ticketService.getStatusByTicket(ticket);
    }

}
