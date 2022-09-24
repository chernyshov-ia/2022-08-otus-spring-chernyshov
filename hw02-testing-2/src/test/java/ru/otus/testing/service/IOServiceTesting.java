package ru.otus.testing.service;

import java.util.Queue;

public class IOServiceTesting implements IOService {

    private Queue<Integer> answers;

    public IOServiceTesting( Queue<Integer> answers ) {
        this.answers = answers;
    }

    @Override
    public void outputString(String s) {

    }

    @Override
    public int readInt() {
        return answers.remove();
    }

    @Override
    public int readIntWithPrompt(String prompt) {
        return answers.remove();
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        return String.valueOf(answers.remove());
    }
}
