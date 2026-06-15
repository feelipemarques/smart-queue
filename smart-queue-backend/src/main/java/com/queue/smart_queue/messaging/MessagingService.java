package com.queue.smart_queue.messaging;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class MessagingService {

    private final RabbitTemplate rabbitTemplate;
    private final JavaMailSender mailSender;
    private final ObjectMapper objectMapper;

    public void publishMessage(NpsEvent message) {
        rabbitTemplate.convertAndSend("", "nps.queue", objectMapper.writeValueAsString(message));
    }

    @RabbitListener(queues = {"nps.queue"})
    public void consumeNpsEvent(String event) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        NpsEvent nps = objectMapper.readValue(event, NpsEvent.class);
        helper.setTo(nps.customerEmail());
        helper.setSubject("How was your experience?");

        String backendUrl = "http://localhost:8080/tickets/" + nps.ticket() + "/nps";

        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<h3>We want to know how was your experience. Please rate us:</h3>");
        htmlContent.append("<div style='display: flex; gap: 8px;'>");

        for (int i = 1; i <= 10; i++) {
            String link = backendUrl + "?score=" + i;
            htmlContent.append("<a href='").append(link).append("' ")
                    .append("style='background-color: #007bff; color: white; padding: 10px 15px; ")
                    .append("text-decoration: none; border-radius: 5px; font-weight: bold;'>")
                    .append(i).append("</a> ");
        }

        htmlContent.append("</div>");
        helper.setText(htmlContent.toString(), true);

        mailSender.send(message);
    }

}
