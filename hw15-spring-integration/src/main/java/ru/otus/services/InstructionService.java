package ru.otus.services;

import org.springframework.stereotype.Component;

@Component
public class InstructionService {
    public String uppercase(String string) {
        return string.toUpperCase();
    }

    public String reverse(String string) {
        return  new StringBuilder(string).reverse().toString();
    }
}
