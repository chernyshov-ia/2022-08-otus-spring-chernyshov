package ru.otus.testing.service;

import org.springframework.stereotype.Service;
import ru.otus.testing.TestFactory;
import ru.otus.testing.domain.Question;
import ru.otus.testing.domain.Test;

@Service
public class TestRunnerServiceImpl implements TestRunnerService {

    private static final int SUCCESS_THRESHOLD_PERCENTS = 80;

    private final IOService ioService;
    private final TestFactory testFactory;

    public TestRunnerServiceImpl(IOService ioService, TestFactory testFactory) {
        this.ioService = ioService;
        this.testFactory = testFactory;
    }


    private int readAnswer(int maxAnswerNumber) {
        int response;
        boolean received;

        do {
            try {
                response = ioService.readIntWithPrompt("Enter number of answer: ");
            } catch (NumberFormatException e) {
                response = 0;
            }
            received = response >= 1 && response <= maxAnswerNumber;

            if (!received) {
                ioService.outputString("No such answer number! Try again.");
            }

        } while (!received);

        return response;
    }

    private boolean askQuestion(Question question) {
        ioService.outputString(question.getText());

        int response;
        for (int i = 0; i < question.getAnswers().size(); i++) {
            ioService.outputString(String.format("  %d. %s\n", i + 1, question.getAnswers().get(i).getText()));
        }

        ioService.outputString("");

        response = readAnswer(question.getAnswers().size());

        boolean isRight = response > 0 && response <= question.getAnswers().size() && question.getAnswers().get(response - 1).isRight();

        if (isRight) {
            ioService.outputString("It's right!");
        } else {
            ioService.outputString("It's wrong!");
        }

        ioService.outputString("");

        return isRight;
    }

    private void performTest(Test test) {
        int rightAnswers = 0;

        ioService.outputString(test.getDescription());
        ioService.outputString("");

        for (Question question : test.getQuestions()) {
            if (askQuestion(question)) {
                rightAnswers++;
            }
        }

        int successPercents = (int) Math.round(100 * rightAnswers / (test.getQuestions().size() * 1.00));
        ioService.outputString(String.format("You result: %d%%", successPercents));

        if(successPercents >= SUCCESS_THRESHOLD_PERCENTS) {
            ioService.outputString("Test is passed!");
        } else {
            ioService.outputString("Test is failed!");
        }
    }


    @Override
    public void perform() {
        var test = testFactory.create();
        performTest(test);
    }
}
