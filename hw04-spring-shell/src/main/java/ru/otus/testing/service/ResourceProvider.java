package ru.otus.testing.service;

import java.io.InputStream;

public interface ResourceProvider {
    InputStream getResourceAsAsStream();
}
