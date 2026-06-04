package com.queue.smart_queue.counter;

import com.queue.smart_queue.exception.CounterNotFoundOrOfflineException;
import com.queue.smart_queue.ticket.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CounterServiceTest {

    @Mock
    private CounterRepository counterRepository;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private CounterService counterService;

    @Test
    public void shouldThrowExceptionWhenCounterIsOff(){
        when(counterRepository.findByIdAndOnline(any(), ArgumentMatchers.eq(true)))
                .thenReturn(Optional.empty());

        assertThrows(CounterNotFoundOrOfflineException.class, () -> counterService.callNext(1L));
    }


}
