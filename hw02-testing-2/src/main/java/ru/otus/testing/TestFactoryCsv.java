package ru.otus.testing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.testing.domain.Test;

@Component
public class TestFactoryCsv implements TestFactory {
    private final String sourceFilename;

    public TestFactoryCsv(@Value("$(app.test.source.filename:questions.csv)") String sourceFilename) {
        if (sourceFilename == null) {
            throw new IllegalArgumentException();
        }
        this.sourceFilename = sourceFilename;
    }

    private Test load() {
        String description;

        return null;
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
