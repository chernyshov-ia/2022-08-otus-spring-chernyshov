package ru.otus.testing.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import ru.otus.testing.config.AppProps;
import ru.otus.testing.domain.Answer;
import ru.otus.testing.domain.Question;
import ru.otus.testing.domain.TestData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CsvTestLoaderTest {
    private final String CSV_FILE_1 = "test1.csv";
    private TestData testData;

    @Configuration
    public static class NestedConfiguration {
    }

    @BeforeEach
    void setUp() {

        var questions = new ArrayList<Question>();

        questions.add(new Question("Question 1", List.of(
                new Answer("Variant 1_1", true),
                new Answer("Variant 1_2", false))));

        questions.add(new Question("Question 2",
                List.of(new Answer("Variant 2_1", false),
                        new Answer("Variant 2_2", true),
                        new Answer("Variant 2_3", false))));

        questions.add(new Question("Question 3",
                List.of(new Answer("Variant 3_1", false),
                        new Answer("Variant 3_2", false),
                        new Answer("Variant 3_3", true))));

        testData = new TestData("Test1", questions);

    }

    @Test
    void when_loading_test_from_csv_then_test_loading_as_expected() {
        var loader = new CsvTestLoader();
        var resourceProvider = new FileQuestionsResourceProvider(Locale.ENGLISH, CSV_FILE_1);
        var loadedTest = loader.load(resourceProvider.getResourceAsAsStream());
        assertThat(loadedTest).usingRecursiveComparison().isEqualTo(testData);
    }

}