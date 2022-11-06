package ru.otus.testing.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import ru.otus.testing.domain.Answer;
import ru.otus.testing.domain.Question;
import ru.otus.testing.domain.TestData;
import ru.otus.testing.domain.TestResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class TestRunnerServiceImplTest {
    private TestData testData;
    private IOService ioService;
    private TestRunnerService testRunner;
    private final int PASS_THRESHOLD_PERCENTS = 80;

    @BeforeEach
    void setUp() {
        List<Question> questions = new ArrayList<>();
        questions.add(new Question("Q1", List.of(new Answer("A1", true), new Answer("A2", false))));
        questions.add(new Question("Q2", List.of(new Answer("A1", true), new Answer("A2", false))));
        questions.add(new Question("Q3", List.of(new Answer("A1", true), new Answer("A2", false))));
        questions.add(new Question("Q4", List.of(new Answer("A1", true), new Answer("A2", false))));
        questions.add(new Question("Q5", List.of(new Answer("A1", true), new Answer("A2", false))));
        testData = new TestData("description", questions);
        var messageSource = Mockito.mock(MessageSource.class);
        var messageService = new LocalizedMessageServiceImpl(messageSource, Locale.ENGLISH);
        ioService = Mockito.mock(IOService.class);
        testRunner = new TestRunnerServiceImpl(ioService, messageService);
    }

    @Test
    void when_test_complete_100_percent_right_then_count_right_answers_is_correct() {
        Mockito.when(ioService.readIntWithPrompt(Mockito.any())).thenReturn(1);
        TestResult result = testRunner.perform(testData);
        assertEquals(result.getRightAnswers(), testData.getQuestionsCount());
        assertTrue(result.isPassed(PASS_THRESHOLD_PERCENTS));
    }

    @Test
    void when_test_complete_0_percent_right_then_count_right_answers_is_correct() {
        Mockito.when(ioService.readIntWithPrompt(Mockito.any())).thenReturn(2);
        TestResult result = testRunner.perform(testData);
        assertEquals(result.getRightAnswers(), 0);
        assertFalse(result.isPassed(PASS_THRESHOLD_PERCENTS));
    }

    @Test
    void when_test_complete_80_percent_right_then_test_is_passed() {
        Mockito.when(ioService.readIntWithPrompt(Mockito.any())).thenReturn(1, 1, 1, 1, 2);
        TestResult result = testRunner.perform(testData);
        assertEquals(result.getRightAnswers(), 4);
        assertTrue(result.isPassed(PASS_THRESHOLD_PERCENTS));
    }

    @Test
    void when_test_complete_60_percent_right_then_test_is_failed() {
        Mockito.when(ioService.readIntWithPrompt(Mockito.any())).thenReturn(1, 1, 1, 2, 2);
        TestResult result = testRunner.perform(testData);
        assertEquals(result.getRightAnswers(), 3);
        assertFalse(result.isPassed(PASS_THRESHOLD_PERCENTS));
    }

}