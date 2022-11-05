package ru.otus.testing.service;

import ru.otus.testing.domain.TestData;

import java.io.InputStream;

public interface TestLoader {
    TestData load(InputStream resource);
}
