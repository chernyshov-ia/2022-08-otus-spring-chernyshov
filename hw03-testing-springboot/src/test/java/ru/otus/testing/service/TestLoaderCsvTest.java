package ru.otus.testing.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestLoaderCsvTest {
    private static final String CSV_FILE_1 = "test1.csv";

    @Test
    void when_loading_test_from_csv_then_test_loading_as_expected() {
        var loader = new TestLoaderCsv(CSV_FILE_1);
        var test = loader.load();
        assertAll(
                () -> assertNotNull(test),
                () -> assertEquals(test.getDescription(), "Test1"),
                () -> assertEquals(test.getQuestionsCount(), 3),
                () -> assertEquals(test.getQuestion(0).getAnswersCount(), 2),
                () -> assertEquals(test.getQuestion(1).getAnswersCount(), 3),
                () -> assertEquals(test.getQuestion(2).getAnswersCount(), 3),
                () -> assertTrue(test.getQuestion(0).getAnswer(0).isRight()),
                () -> assertTrue(test.getQuestion(1).getAnswer(1).isRight()),
                () -> assertTrue(test.getQuestion(2).getAnswer(2).isRight()),
                () -> assertFalse(test.getQuestion(0).getAnswer(1).isRight()),
                () -> assertEquals(test.getQuestion(0).getText(), "Question 1"),
                () -> assertEquals(test.getQuestion(0).getAnswer(0).getText(), "Variant 1_1"),
                () -> assertEquals(test.getQuestion(0).getAnswer(1).getText(), "Variant 1_2"),
                () -> assertEquals(test.getQuestion(1).getText(), "Question 2"),
                () -> assertEquals(test.getQuestion(2).getText(), "Question 3"),
                () -> assertEquals(test.getQuestion(2).getAnswer(0).getText(), "Variant 3_1")
        );
    }

}