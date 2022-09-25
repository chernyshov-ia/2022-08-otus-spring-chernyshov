package ru.otus.testing.context;

import org.springframework.stereotype.Component;

@Component
public class UserContext {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
