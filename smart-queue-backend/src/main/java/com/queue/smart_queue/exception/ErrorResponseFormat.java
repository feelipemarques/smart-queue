package com.queue.smart_queue.exception;

import java.time.LocalDateTime;

public record ErrorResponseFormat(LocalDateTime timeStamp, String status, String message) {
}
