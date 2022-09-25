package ru.otus.testing;

import org.springframework.stereotype.Component;
import ru.otus.testing.config.AppProps;
import ru.otus.testing.domain.TestData;
import ru.otus.testing.domain.TestResult;
import ru.otus.testing.service.IOService;
import ru.otus.testing.service.TestLoader;
import ru.otus.testing.service.TestRunnerService;

@Component
public class Application {
    private final TestRunnerService testRunnerService;
    private final IOService ioService;
    private final TestLoader testLoader;
    private final AppProps props;

    public Application(TestRunnerService testRunnerService, IOService ioService, TestLoader testLoader, AppProps props) {
        this.testRunnerService = testRunnerService;
        this.ioService = ioService;
        this.testLoader = testLoader;
        this.props = props;
    }

    public void run() {
        String studentName = ioService.readStringWithPrompt("Enter your name: ");
        TestData test = testLoader.load();
        TestResult result = testRunnerService.perform(test);
        outputTestResult(studentName, result);
    }

    private void outputTestResult(String studentName, TestResult testResult) {
        if (testResult.isPassed(props.getPassThresholdPercents())) {
            ioService.outputString("Student " + studentName + " passed the test!");
        } else {
            ioService.outputString("Student " + studentName + " failed the test!");
        }
    }
}
