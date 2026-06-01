package com.queue.smart_queue.ticket;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public TicketResponse getTicket(@RequestParam("prefix") TicketPrefix prefix) {
        return ticketService.issueTicket(prefix);
    }

    @GetMapping
    public List<TicketResponse> getOpenTickets(){
        return ticketService.getOpenTickets();
    }

    @GetMapping("/{ticket}")
    public TicketStatus getTicketStatus(@PathVariable("ticket") String ticket){
        return ticketService.getStatusByTicket(ticket);
    }

}
