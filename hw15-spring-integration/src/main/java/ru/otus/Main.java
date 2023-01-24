package ru.otus;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import ru.otus.services.TaskProcessor;
import ru.otus.domain.Instruction;
import ru.otus.domain.Task;

@SpringBootApplication
public class Main {

    private ApplicationContext context;

    public Main(ApplicationContext context) {
        this.context = context;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
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
