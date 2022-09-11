package ru.otus.testing;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.testing.domain.Answer;
import ru.otus.testing.domain.Question;
import ru.otus.testing.domain.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TestFactoryCsv implements TestFactory {
    private final String sourceFilename;

    public TestFactoryCsv(@Value("$(app.test.source.filename:questions.csv)") String sourceFilename) {
        if (sourceFilename == null) {
            throw new IllegalArgumentException();
        }
        this.sourceFilename = sourceFilename;
    }

    private List<String[]> loadCsv() {
        try ( var stream = TestFactoryCsv.class.getClassLoader().getResourceAsStream(sourceFilename) ) {

            if ( stream == null ) {
                throw new RuntimeException("Resource not found");
            }

            try (var streamReader = new InputStreamReader(stream);
                 CSVReader reader = new CSVReader(streamReader)) {
                List<String[]> r = reader.readAll();
                return r;
            }

        } catch (IOException | CsvException e) {
            throw new RuntimeException("Can't load resource", e);
        }
    }


    private String extractDescription(List<String[]> csv) {
        if (csv.size() == 0 || csv.get(0).length != 1) {
            throw new RuntimeException("Unsupported format of data in csv");
        }
        return csv.get(0)[0];
    }

    private Answer mapToAnswer(String[] strings) {
        var text = strings[1];
        var isRight = !("0".equals(strings[2]) || strings[2] == null);
        return new Answer(text, isRight);
    }

    private List<Question> extractQuestions(List<String[]> csv) {
        List<Question> questions = new ArrayList<>();

        var map = csv.stream()
                .filter(x -> x.length == 3)
                .collect(Collectors.groupingBy(x -> x[0]));

        for (var e : map.entrySet()) {
            List<Answer> answers = e.getValue().stream().map(this::mapToAnswer).collect(Collectors.toList());
            questions.add(new Question(e.getKey(), answers));
        }

        return questions;
    }

    private Test load() {
        var csv = loadCsv();
        var description = extractDescription(csv);
        var questions = extractQuestions(csv);

        if (questions.size() == 0) {
            throw new RuntimeException("Questions and answers not found in resource");
        }

        return new Test(description, questions);
    }

    @Override
    public Test create() {
        try {
            return load();
        } catch (Throwable e) {
            throw new TestInstantiationException("can't instantiate Test", e);
        }
    }
}
