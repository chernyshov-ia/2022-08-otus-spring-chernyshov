package ru.otus.testing;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.testing.domain.Test;

public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext  context = new AnnotationConfigApplicationContext(Main.class);

        Test test = context.getBean( Test.class );
//        test.printQuestions();

        context.close();
    }
}
