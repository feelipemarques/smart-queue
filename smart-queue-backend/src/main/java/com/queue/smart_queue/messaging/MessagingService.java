package com.queue.smart_queue.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagingService {

    private final RabbitTemplate rabbitTemplate;
    private final JavaMailSender mailSender;

    public void publishMessage(NpsEvent message) {
        rabbitTemplate.convertAndSend("", "nps.queue", message.customerEmail());
    }

    @RabbitListener(queues = {"nps.queue"})
    public void consumeNpsEvent(NpsEvent event){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.customerEmail());
        message.setSubject("How was your experience?");
        message.setText("We want to know how was your experience. Please rate from 1 to 10.");
        mailSender.send(message);
    }

}
