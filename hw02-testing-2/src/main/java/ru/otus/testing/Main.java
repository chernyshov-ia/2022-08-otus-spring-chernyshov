package ru.otus.testing;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import ru.otus.testing.service.TestRunnerService;

@ComponentScan
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext  context = new AnnotationConfigApplicationContext(Main.class);

        TestRunnerService runner = context.getBean( TestRunnerService.class );
        runner.perform();

        context.close();
    }
}
