package com.queue.smart_queue.counter;

import com.queue.smart_queue.exception.CounterNotFoundOrOfflineException;
import com.queue.smart_queue.ticket.TicketCalledResponse;
import com.queue.smart_queue.ticket.TicketService;
import com.queue.smart_queue.ticket.TicketStatus;
import com.queue.smart_queue.websocket.dto.TicketCalled;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounterService {

    private final CounterRepository counterRepository;
    private final TicketService ticketService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public CounterResponse createCounter(){
        Counter counter = new Counter();
        counterRepository.save(counter);
        return new CounterResponse(counter.getId());
    }

    public void changeCounterStatus(Long counterId, Boolean status){
        counterRepository.findById(counterId).ifPresent(counter -> {
            counter.setOnline(status);
            counterRepository.save(counter);
        });
    }

    public Boolean getCounterStatus(Long counterId){
        Counter counter = counterRepository.findById(counterId).orElseThrow(() -> new CounterNotFoundOrOfflineException("Counter not found"));
        return counter.getOnline();
    }

    public TicketCalledResponse callNext(Long counterId){
        var ticket = ticketService.callNext(counterRepository
                .findByIdAndOnline(counterId, Boolean.TRUE)
                .orElseThrow(()-> new CounterNotFoundOrOfflineException("Counter not found or offline!")));

        simpMessagingTemplate.convertAndSend("/topic/ticket/" + ticket.number(), new TicketCalled(TicketStatus.IN_SERVICE, counterId));
        simpMessagingTemplate.convertAndSend("/topic/queue", ticketService.getOpenTickets());
        return ticket;
    }

    public void finishTicket(Long counterId, String number){
        Counter counter = counterRepository.
                findByIdAndOnline(counterId, Boolean.TRUE)
                .orElseThrow(()-> new CounterNotFoundOrOfflineException("Counter not found or offline!"));
        ticketService.updateStatus(number, TicketStatus.FINISHED, counter);
        simpMessagingTemplate.convertAndSend("/topic/ticket/" + number, new TicketCalled(TicketStatus.FINISHED, counterId));
    }





}
