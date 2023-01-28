package ru.otus.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.domain.Task;

@MessagingGateway
public interface TaskProcessor {
    @Gateway(requestChannel = "taskChannel", replyChannel = "resultChannel")
    String process(Task task);
}