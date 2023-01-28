package ru.otus.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.domain.Instruction;
import ru.otus.domain.Task;
import ru.otus.services.TaskProcessor;

@Configuration
public class AppConfig {
    private final ApplicationContext context;

    public AppConfig(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    ApplicationRunner run(ApplicationArguments args) throws Exception {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                TaskProcessor gateway = context.getBean(TaskProcessor.class);
                System.out.println(gateway.process(new Task(Instruction.UPPERCASE, "message text")));
                System.out.println(gateway.process(new Task(Instruction.REVERSE_WORDS, "message,text,example,а роза упала на лапу азора")));
            }
        };
    }
}
