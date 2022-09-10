package ru.otus.testing.domain;

import java.util.Collections;
import java.util.List;

public class Test {
    private final String description;
    private final List<? extends Question> questions;

    public Test(String description, List<? extends Question> questions) {
        this.description = description;
        this.questions = Collections.unmodifiableList(questions);
    }

    public String getDescription() {
        return description;
    }

    public List<? extends Question> getQuestions() {
        return questions;
    }
}
