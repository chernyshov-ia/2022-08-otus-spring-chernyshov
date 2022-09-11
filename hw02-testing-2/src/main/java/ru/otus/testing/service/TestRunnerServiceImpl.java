package ru.otus.testing.service;

import org.springframework.stereotype.Service;
import ru.otus.testing.domain.Question;
import ru.otus.testing.domain.Test;
import ru.otus.testing.domain.TestResult;

@Service
public class TestRunnerServiceImpl implements TestRunnerService {
    private final IOService ioService;

    public TestRunnerServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }

    private boolean askQuestion(Question question) {
        ioService.outputString(question.getText());

        int response;
        for (int i = 0; i < question.getAnswers().size(); i++) {
            ioService.outputString(String.format("  %d. %s", i + 1, question.getAnswers().get(i).getText()));
        }

        try {
            response = ioService.readIntWithPrompt("Enter number of answer: ");
        } catch (NumberFormatException e) {
            response = -1;
        }

        return response > 0 && response <= question.getAnswers().size() && question.getAnswers().get(response - 1).isRight();
    }

    @Override
    public TestResult perform(Test test) {
        int rightAnswers = 0;

        ioService.outputString("Let's start: " + test.getDescription());

        for (Question question : test.getQuestions()) {
            if (askQuestion(question)) {
                rightAnswers++;
            }
        }

        return new TestResult(rightAnswers, test.getQuestions().size());
    }
}
