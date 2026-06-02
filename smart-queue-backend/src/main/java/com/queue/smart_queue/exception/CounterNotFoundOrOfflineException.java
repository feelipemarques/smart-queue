package com.queue.smart_queue.exception;

public class CounterNotFoundOrOfflineException extends RuntimeException {
    public CounterNotFoundOrOfflineException(String message) {
        super(message);
    }
}
