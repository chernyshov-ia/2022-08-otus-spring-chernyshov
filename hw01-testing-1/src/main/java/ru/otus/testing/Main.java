package ru.otus.testing;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.testing.domain.Test;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        Test test = context.getBean( Test.class );
        test.printQuestions();

        context.close();
    }
}
