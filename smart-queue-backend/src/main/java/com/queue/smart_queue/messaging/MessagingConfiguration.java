package com.queue.smart_queue.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfiguration {
    @Bean
    public Queue npsQueue(){
        return new Queue("nps.queue");
    }
}
