package com.queue.smart_queue.counter;

import com.queue.smart_queue.ticket.TicketCalledResponse;
import com.queue.smart_queue.ticket.TicketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CounterController {

    private final CounterService counterService;

    @PostMapping("/counter/new")
    public String newCounter(){
        return counterService.createCounter().toString();
    }

    @PutMapping("/counter/{counterId}/status")
    public void changeCounterStatus(@PathVariable("counterId") Long counterId, @RequestParam("on") Boolean status){
        counterService.changeCounterStatus(counterId, status);
    }

    @PostMapping("/counter/{counterId}/call")
    public TicketCalledResponse callNextTicket(@PathVariable Long counterId){
        return counterService.callNext(counterId);
    }

    @PutMapping("/counter/{counterId}/finish")
    public void finishCounter(@PathVariable("counterId") Long counterId, @RequestParam("ticket") String ticket){
        counterService.finishTicket(counterId, ticket);
    }

}
