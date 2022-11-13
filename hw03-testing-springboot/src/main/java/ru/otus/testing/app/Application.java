package ru.otus.testing.app;

import org.springframework.stereotype.Component;
import ru.otus.testing.config.AppProps;
import ru.otus.testing.domain.TestData;
import ru.otus.testing.domain.TestResult;
import ru.otus.testing.service.*;

@Component
public class Application {
    private final TestRunnerService testRunnerService;
    private final IOService ioService;
    private final TestLoader testLoader;
    private final LocalizedMessageService messageService;
    private final AppProps props;

    public Application(TestRunnerService testRunnerService, IOService ioService, TestLoader testLoader,
                       LocalizedMessageService messageService, AppProps props) {
        this.testRunnerService = testRunnerService;
        this.ioService = ioService;
        this.testLoader = testLoader;
        this.messageService = messageService;
        this.props = props;
    }

    public void run() {
        var prompt = messageService.getMessage("app.queryName");
        String studentName = ioService.readStringWithPrompt(prompt + ": ");
        TestData test = testLoader.load();
        TestResult result = testRunnerService.perform(test);
        outputTestResult(studentName, result);
    }

    private void outputTestResult(String studentName, TestResult testResult) {
        if (testResult.isPassed(props.getPassThresholdPercents())) {
            var msg = messageService.getMessage("app.studentPassedTest", new String[]{studentName});
            ioService.outputString(msg);
        } else {
            var msg = messageService.getMessage("app.studentFailedTest", new String[]{studentName});
            ioService.outputString(msg);
        }
    }
}
