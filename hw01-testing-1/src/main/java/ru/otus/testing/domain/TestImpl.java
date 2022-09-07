package ru.otus.testing.domain;

import org.springframework.stereotype.Component;
import ru.otus.testing.TestLoaderStrategy;

import java.util.List;

public class TestImpl implements Test {
    final private List<? extends Question> questions;

    public TestImpl(TestLoaderStrategy loaderStrategy) {
        if (loaderStrategy == null) throw new IllegalArgumentException();
        this.questions = loaderStrategy.loadQuestions();
    }

    private void printQuestion(Question question){
        System.out.println(question.getText());
        for (int i = 0; i < question.getAnswers().size(); i++) {
            System.out.format("  %d. %s\n", i + 1, question.getAnswers().get(i).getText());
        }
        System.out.println();
    }

    @Override
    public void printQuestions() {
        for (Question question : questions) {
            printQuestion(question);
        }
    }
}
