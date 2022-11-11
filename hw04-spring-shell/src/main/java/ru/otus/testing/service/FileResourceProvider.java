package ru.otus.testing.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class FileResourceProvider implements ResourceProvider {
    private final FilenameProvider filenameProvider;

    public FileResourceProvider(FilenameProvider filenameProvider) {
        this.filenameProvider = filenameProvider;
    }

    @Override
    public InputStream getResourceAsAsStream() {
        var resourceAsStream = ResourceProvider.class.getClassLoader().getResourceAsStream(filenameProvider.getFilename());
        if ( resourceAsStream == null ) {
            throw new RuntimeException("Resource not found");
        }
        return resourceAsStream;
    }

}
