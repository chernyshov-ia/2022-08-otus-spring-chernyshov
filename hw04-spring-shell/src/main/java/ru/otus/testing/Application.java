package ru.otus.testing;

import org.springframework.context.MessageSource;
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
    private final MessageSource messageSource;
    private final AppProps props;

    public Application(TestRunnerService testRunnerService, IOService ioService, TestLoader testLoader,
                       MessageSource messageSource, AppProps props) {
        this.testRunnerService = testRunnerService;
        this.ioService = ioService;
        this.testLoader = testLoader;
        this.messageSource = messageSource;
        this.props = props;
    }

    public void run() {
        var prompt = messageSource.getMessage("app.queryName", new String[]{}, props.getLocale());
        String studentName = ioService.readStringWithPrompt(prompt + ": ");
        TestData test = testLoader.load();
        TestResult result = testRunnerService.perform(test);
        outputTestResult(studentName, result);
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
