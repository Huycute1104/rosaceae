package com.example.rosaceae.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;

    public void SendMail(SimpleMailMessage simpleMailMessage) {
        Destination destination = new Destination();
        destination.setToAddresses(Arrays.asList(Objects.requireNonNull(simpleMailMessage.getTo())));

        Content content = new Content();
        content.setData(simpleMailMessage.getText());

        Body body = new Body();
        body.setHtml(content);

        Content subject = new Content();
        subject.setData(simpleMailMessage.getSubject());

        Message message = new Message();
        message.setSubject(subject);
        message.setBody(body);

        SendEmailRequest emailRequest = new SendEmailRequest();
        emailRequest.setDestination(destination);
        emailRequest.setMessage(message);
        emailRequest.setSource(simpleMailMessage.getFrom());

        amazonSimpleEmailService.sendEmail(emailRequest);
    }
}
