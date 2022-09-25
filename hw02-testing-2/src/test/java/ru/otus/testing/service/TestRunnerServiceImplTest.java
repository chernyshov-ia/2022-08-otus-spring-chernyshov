package ru.otus.testing.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.otus.testing.domain.Answer;
import ru.otus.testing.domain.Question;
import ru.otus.testing.domain.TestData;
import ru.otus.testing.domain.TestResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TestRunnerServiceImplTest {
    static TestData testData;

    @BeforeAll
    static void init() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Q1", List.of(new Answer("A1", true), new Answer("A2", false))));
        questions.add(new Question("Q2", List.of(new Answer("A1", true), new Answer("A2", false))));
        questions.add(new Question("Q3", List.of(new Answer("A1", true), new Answer("A2", false))));
        questions.add(new Question("Q4", List.of(new Answer("A1", true), new Answer("A2", false))));
        questions.add(new Question("Q5", List.of(new Answer("A1", true), new Answer("A2", false))));
        testData = new TestData("description", questions);
    }

    @Test
    void when_test_complete_100_percent_right_then_count_right_answers_is_correct() {
        var rightAnswers = new ArrayDeque<>(List.of(1,1,1,1,1));
        IOServiceTesting ioServiceTesting = new IOServiceTesting(rightAnswers);
        var testRunner = new TestRunnerServiceImpl(ioServiceTesting);
        TestResult result = testRunner.perform(testData);
        assertEquals(result.getRightAnswers(), testData.getQuestionsCount());
        assertTrue(result.isPassed());
    }

    @Test
    void when_test_complete_0_percent_right_then_count_right_answers_is_correct() {
        var rightAnswers = new ArrayDeque<>(List.of(2,2,2,2,2));
        IOServiceTesting ioServiceTesting = new IOServiceTesting(rightAnswers);
        var testRunner = new TestRunnerServiceImpl(ioServiceTesting);
        TestResult result = testRunner.perform(testData);
        assertEquals(result.getRightAnswers(), 0);
        assertFalse(result.isPassed());
    }

    @Test
    void when_test_complete_80_percent_right_then_test_is_passed() {
        var rightAnswers = new ArrayDeque<>(List.of(1,1,1,1,2));
        IOServiceTesting ioServiceTesting = new IOServiceTesting(rightAnswers);
        var testRunner = new TestRunnerServiceImpl(ioServiceTesting);
        TestResult result = testRunner.perform(testData);
        assertEquals(result.getRightAnswers(), 4);
        assertTrue(result.isPassed());
    }

    @Test
    void when_test_complete_60_percent_right_then_test_is_failed() {
        var rightAnswers = new ArrayDeque<>(List.of(1,1,1,2,2));
        IOServiceTesting ioServiceTesting = new IOServiceTesting(rightAnswers);
        var testRunner = new TestRunnerServiceImpl(ioServiceTesting);
        TestResult result = testRunner.perform(testData);
        assertEquals(result.getRightAnswers(), 3);
        assertFalse(result.isPassed());
    }

}