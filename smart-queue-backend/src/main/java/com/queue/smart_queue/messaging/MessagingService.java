package com.queue.smart_queue.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingService {

    private final RabbitTemplate rabbitTemplate;

    public void publishMessage(NpsEvent message) {
        rabbitTemplate.convertAndSend("", "nps.queue", message.customerEmail());
    }

}
