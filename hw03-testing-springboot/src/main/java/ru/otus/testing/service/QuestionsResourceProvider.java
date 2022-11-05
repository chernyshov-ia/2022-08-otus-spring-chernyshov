package ru.otus.testing.service;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Locale;

@Component
public class QuestionsResourceProvider implements ResourceProvider {
    private final Locale locale;
    private final String resourceFilename;

    public QuestionsResourceProvider(Locale locale, String resourceFilename) {
        this.locale = locale;
        this.resourceFilename = resourceFilename;
    }

    @Override
    public InputStream getResourceAsAsStream() {
        String filename = StringUtils.stripFilenameExtension(resourceFilename);
        String filenameExt = StringUtils.getFilenameExtension(resourceFilename);

        String resourceFilename;
        if (locale.equals(Locale.ENGLISH)) {
            resourceFilename = filename + "." + filenameExt;
        } else {
            resourceFilename = filename + "_" + locale + "." + filenameExt;
        }

        var resourceAsStream = ResourceProvider.class.getClassLoader().getResourceAsStream(resourceFilename);
        if ( resourceAsStream == null ) {
            throw new RuntimeException("Resource not found");
        }

        return resourceAsStream;
    }

}
