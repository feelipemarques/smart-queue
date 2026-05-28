package com.queue.smart_queue.counter;

import com.queue.smart_queue.ticket.TicketCalledResponse;
import com.queue.smart_queue.ticket.TicketResponse;
import com.queue.smart_queue.ticket.TicketService;
import com.queue.smart_queue.ticket.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounterService {

    private final CounterRepository counterRepository;
    private final TicketService ticketService;

    public Long createCounter(){
        Counter counter = new Counter();
        counterRepository.save(counter);
        return counter.getId();
    }

    public void changeCounterStatus(Long counterId, Boolean status){
        counterRepository.findById(counterId).ifPresent(counter -> {
            counter.setOnline(status);
            counterRepository.save(counter);
        });
    }

    public TicketCalledResponse callNext(Long counterId){
        return ticketService.callNext(counterRepository
                .findByIdAndOnline(counterId, Boolean.TRUE)
                .orElseThrow(()-> new RuntimeException("Counter not found or offline!")));
    }

    public void finishTicket(Long counterId, String number){
        Counter counter = counterRepository.
                findByIdAndOnline(counterId, Boolean.TRUE)
                .orElseThrow(()-> new RuntimeException("Counter not found or offline!"));
        ticketService.updateStatus(number, TicketStatus.FINISHED, counter);
    }





}
