package ru.otus.testing;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;
import ru.otus.testing.domain.Answer;
import ru.otus.testing.domain.Question;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class CsvTestLoaderStrategy implements TestLoaderStrategy {

    private final String fileName;

    private Answer mapToAnswer(String[] strings) {
        var text = strings[1];
        var isRight = !("0".equals(strings[2]) || strings[2] == null);
        return new Answer(text, isRight);
    }

    @Override
    public List<? extends Question> loadQuestions() {
        var questions = new ArrayList<Question>();

        try ( var stream = CsvTestLoaderStrategy.class.getClassLoader().getResourceAsStream(fileName) ) {

            if ( stream == null ) {
                throw new RuntimeException("Ресурс не найден");
            }

            try ( var streamReader = new InputStreamReader(stream);
                  CSVReader reader = new CSVReader(streamReader))
            {
                List<String[]> r = reader.readAll();
                var map = r.stream().filter(x -> x.length == 3).collect(Collectors.groupingBy(x -> x[0]));
                for (var e : map.entrySet()) {
                    List<Answer> answers = e.getValue().stream().map(this::mapToAnswer).collect(Collectors.toList());
                    questions.add(new Question(e.getKey(), answers));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Не могу загрузить тесты", e);
        }

        return questions;
    }

    public CsvTestLoaderStrategy(String fileName) {
        this.fileName = fileName;
    }
}
