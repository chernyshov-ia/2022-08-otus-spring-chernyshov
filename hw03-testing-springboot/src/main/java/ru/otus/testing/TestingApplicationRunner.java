package ru.otus.testing;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class TestingApplicationRunner implements ApplicationRunner {
    private final Application application;

    public TestingApplicationRunner(Application application) {
        this.application = application;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        application.run();
    }
}
