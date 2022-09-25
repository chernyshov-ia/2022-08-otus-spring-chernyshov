package ru.otus.testing.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.testing.config.AppProps;
import ru.otus.testing.domain.Question;
import ru.otus.testing.domain.TestData;
import ru.otus.testing.domain.TestResult;

@Service
public class TestRunnerServiceImpl implements TestRunnerService {
    private final IOService ioService;
    private final AppProps props;

    public TestRunnerServiceImpl(IOService ioService, AppProps props) {
        this.ioService = ioService;
        this.props = props;
    }

    private boolean askQuestion(Question question) {
        ioService.outputString(question.getText());

        int response;
        for (int i = 0; i < question.getAnswersCount(); i++) {
            ioService.outputString(String.format("  %d. %s", i + 1, question.getAnswer(i).getText()));
        }

        try {
            response = ioService.readIntWithPrompt("Enter number of answer: ");
        } catch (NumberFormatException e) {
            response = -1;
        }

        return response > 0 && response <= question.getAnswersCount() && question.getAnswer(response - 1).isRight();
    }

    @Override
    public TestResult perform(TestData test) {
        int rightAnswers = 0;

        ioService.outputString("Let's start: " + test.getDescription());

        for (int i = 0; i < test.getQuestionsCount(); i++) {
            if (askQuestion(test.getQuestion(i))) {
                rightAnswers++;
            }
        }

        return new TestResult(rightAnswers, test.getQuestions().size());
    }
}
