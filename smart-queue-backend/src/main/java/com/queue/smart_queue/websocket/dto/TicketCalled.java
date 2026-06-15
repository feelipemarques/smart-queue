package com.queue.smart_queue.websocket.dto;

import com.queue.smart_queue.ticket.TicketStatus;


public record TicketCalled(TicketStatus ticketStatus, Long counterId) {
}
