package com.queue.smart_queue.ticket;

import java.time.LocalDateTime;

public record TicketResponse(String number, LocalDateTime issuedAt) {
}
