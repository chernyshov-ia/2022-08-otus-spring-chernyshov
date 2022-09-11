package ru.otus.testing.service;

import ru.otus.testing.domain.Test;
import ru.otus.testing.domain.TestResult;

public interface TestRunnerService {
    TestResult perform(Test test);
}
