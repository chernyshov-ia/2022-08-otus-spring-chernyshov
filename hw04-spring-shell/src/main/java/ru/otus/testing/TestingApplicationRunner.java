package ru.otus.testing;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class TestingApplicationRunner implements ApplicationRunner {
    private final ApplicationContext context;

    public TestingApplicationRunner(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Application application = context.getBean(Application.class);
        application.run();
    }
}
