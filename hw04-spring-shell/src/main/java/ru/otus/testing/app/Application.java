package ru.otus.testing.app;

import org.springframework.stereotype.Component;
import ru.otus.testing.config.AppProps;
import ru.otus.testing.context.UserContext;
import ru.otus.testing.domain.TestData;
import ru.otus.testing.domain.TestResult;
import ru.otus.testing.service.*;

import java.io.IOException;
import java.io.InputStream;

@Component
public class Application {
    private final TestRunnerService testRunnerService;
    private final IOService ioService;
    private final TestLoader testLoader;
    private final LocalizedMessageService messageService;
    private final AppProps props;
    private final QuestionsResourceProvider resourceProvider;
    private final UserContext userContext;

    public Application(TestRunnerService testRunnerService, IOService ioService, TestLoader testLoader,
                       LocalizedMessageService messageService, AppProps props, QuestionsResourceProvider resourceProvider,
                       UserContext userContext) {
        this.testRunnerService = testRunnerService;
        this.ioService = ioService;
        this.testLoader = testLoader;
        this.messageService = messageService;
        this.props = props;
        this.resourceProvider = resourceProvider;
        this.userContext = userContext;
    }

    public void run() {
        if (userContext.getUsername() == null || "".equals(userContext.getUsername().trim())) {
            var msg = messageService.getMessage("runner.unknownUser");
            ioService.outputString(msg);
            return;
        }
        try ( InputStream resourceAsStream = resourceProvider.getResourceAsAsStream() ) {
            TestData test = testLoader.load(resourceAsStream);
            TestResult result = testRunnerService.perform(test);
            outputTestResult(userContext.getUsername(), result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
