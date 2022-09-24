package ru.otus.testing.domain;

import java.util.Collections;
import java.util.List;

public class Question {
    final private String text;
    final private List<Answer> answers;

    public Question(String text, List<Answer> answers) {
        this.text = text;
        this.answers = answers;
    }

    public String getText() {
        return text;
    }

    public int getAnswersCount() {
        return answers.size();
    }

    public Answer getAnswer(int index) {
        return answers.get(index);
    }
}
