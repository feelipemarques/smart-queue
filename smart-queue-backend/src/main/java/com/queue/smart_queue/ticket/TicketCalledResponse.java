package com.queue.smart_queue.ticket;

import java.time.LocalDateTime;

public record TicketCalledResponse(String number, LocalDateTime issuedAt, Long counter) {
}
