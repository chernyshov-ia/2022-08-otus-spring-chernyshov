package ru.otus.testing.domain;

import java.util.Collections;
import java.util.List;

public class TestData {
    private final String description;
    private final List<Question> questions;

    public TestData(String description, List<Question> questions) {
        this.description = description;
        this.questions = Collections.unmodifiableList(questions);
    }

    public String getDescription() {
        return description;
    }

    public Question getQuestion(int index) {
        return questions.get(index);
    }

    public int getQuestionsCount() {
        return questions.size();
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
