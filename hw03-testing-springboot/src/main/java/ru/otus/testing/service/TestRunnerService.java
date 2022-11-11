package ru.otus.testing.service;

import ru.otus.testing.domain.TestData;
import ru.otus.testing.domain.TestResult;

public interface TestRunnerService {
    TestResult perform(TestData test);
}
