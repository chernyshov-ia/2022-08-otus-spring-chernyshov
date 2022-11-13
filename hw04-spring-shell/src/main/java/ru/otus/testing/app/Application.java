package ru.otus.testing.app;

import org.springframework.stereotype.Component;
import ru.otus.testing.config.AppProps;
import ru.otus.testing.context.UserContext;
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
    private final UserContext userContext;

    public Application(TestRunnerService testRunnerService, IOService ioService, TestLoader testLoader,
                       LocalizedMessageService messageService, AppProps props, ResourceProvider resourceProvider,
                       UserContext userContext) {
        this.testRunnerService = testRunnerService;
        this.ioService = ioService;
        this.testLoader = testLoader;
        this.messageService = messageService;
        this.props = props;
        this.userContext = userContext;
    }

    public void run() {
        if (userContext.getUsername() == null || "".equals(userContext.getUsername().trim())) {
            var msg = messageService.getMessage("runner.unknownUser");
            ioService.outputString(msg);
            return;
        }
        TestData test = testLoader.load();
        TestResult result = testRunnerService.perform(test);
        outputTestResult(userContext.getUsername(), result);
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
