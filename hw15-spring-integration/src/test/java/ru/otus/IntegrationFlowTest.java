package ru.otus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.domain.Instruction;
import ru.otus.domain.Task;
import ru.otus.services.TaskProcessor;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class IntegrationFlowTest {
    public static final String STRING_1 = "abc";
    public static final String STRING_2 = "123,456";
    public static final String STRING_2_PROCESSED = "321,654";

    @Autowired
    TaskProcessor processor;

    @Test
    void shouldReturnCorrectStringUppercaseTask() {
        Task t = new Task(Instruction.UPPERCASE, STRING_1);
        var s = processor.process(t);
        assertThat(s).isEqualTo(STRING_1.toUpperCase());
    }

    @Test
    void shouldReturnCorrectStringReverseWorldTask() {
        Task t = new Task(Instruction.REVERSE_WORDS, STRING_2);
        var s = processor.process(t);
        assertThat(s).isEqualTo(STRING_2_PROCESSED);
    }
}
