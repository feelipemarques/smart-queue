package com.queue.smart_queue.counter;

import com.queue.smart_queue.messaging.NpsEvent;
import com.queue.smart_queue.ticket.TicketCalledResponse;
import com.queue.smart_queue.ticket.TicketResponse;
import com.queue.smart_queue.ticket.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/counter")
@RequiredArgsConstructor
public class CounterController {

    private final CounterService counterService;

    @PostMapping("/new")
    public ResponseEntity<CounterResponse> newCounter(){
        return ResponseEntity.status(HttpStatus.CREATED).body(counterService.createCounter());
    }

    @PutMapping("/{counterId}/status")
    public void changeCounterStatus(@PathVariable("counterId") Long counterId, @RequestParam("on") Boolean status){
        counterService.changeCounterStatus(counterId, status);
    }

    @GetMapping("/{counterId}/status")
    public Boolean getCounterStatus(@PathVariable("counterId") Long counterId){
       return counterService.getCounterStatus(counterId);
    }

    @PostMapping("/{counterId}/call")
    public TicketCalledResponse callNextTicket(@PathVariable Long counterId){
        return counterService.callNext(counterId);
    }

    @PutMapping("/{counterId}/finish")
    public void finishTicket(@PathVariable("counterId") Long counterId, @RequestParam("ticket") String ticket, @RequestBody NpsEvent event){
        counterService.finishTicket(counterId, ticket, event);
    }

}
