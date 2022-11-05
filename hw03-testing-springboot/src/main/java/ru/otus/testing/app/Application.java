package ru.otus.testing.app;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.otus.testing.config.AppProps;
import ru.otus.testing.domain.TestData;
import ru.otus.testing.domain.TestResult;
import ru.otus.testing.service.IOService;
import ru.otus.testing.service.ResourceProvider;
import ru.otus.testing.service.TestLoader;
import ru.otus.testing.service.TestRunnerService;

import java.io.IOException;
import java.io.InputStream;

@Component
public class Application {
    private final TestRunnerService testRunnerService;
    private final IOService ioService;
    private final TestLoader testLoader;
    private final MessageSource messageSource;
    private final AppProps props;
    private final ResourceProvider resourceProvider;

    public Application(TestRunnerService testRunnerService, IOService ioService, TestLoader testLoader,
                       MessageSource messageSource, AppProps props, ResourceProvider resourceProvider) {
        this.testRunnerService = testRunnerService;
        this.ioService = ioService;
        this.testLoader = testLoader;
        this.messageSource = messageSource;
        this.props = props;
        this.resourceProvider = resourceProvider;
    }

    public void run() {
        var prompt = messageSource.getMessage("app.queryName", new String[]{}, props.getLocale());
        String studentName = ioService.readStringWithPrompt(prompt + ": ");
        try ( InputStream resourceAsStream = resourceProvider.getResourceAsAsStream(); ) {
            TestData test = testLoader.load(resourceAsStream);
            TestResult result = testRunnerService.perform(test);
            outputTestResult(studentName, result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void outputTestResult(String studentName, TestResult testResult) {
        if (testResult.isPassed(props.getPassThresholdPercents())) {
            var msg = messageSource.getMessage("app.studentPassedTest", new String[]{studentName}, props.getLocale());
            ioService.outputString(msg);
        } else {
            var msg = messageSource.getMessage("app.studentFailedTest", new String[]{studentName}, props.getLocale());
            ioService.outputString(msg);
        }
    }
}
