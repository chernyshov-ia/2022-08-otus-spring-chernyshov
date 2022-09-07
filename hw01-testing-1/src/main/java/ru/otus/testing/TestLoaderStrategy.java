package ru.otus.testing;

import ru.otus.testing.domain.Question;

import java.util.List;

public interface TestLoaderStrategy {
    List<? extends Question> loadQuestions();
}
