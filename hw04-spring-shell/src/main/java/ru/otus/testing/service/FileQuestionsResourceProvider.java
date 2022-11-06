package ru.otus.testing.service;

import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Locale;

public class FileQuestionsResourceProvider implements QuestionsResourceProvider {
    private final Locale locale;
    private final String resourceFilename;

    public FileQuestionsResourceProvider(Locale locale, String resourceFilename) {
        this.locale = locale;
        this.resourceFilename = resourceFilename;
    }

    private String getResourceFilename() {
        String filename = StringUtils.stripFilenameExtension(resourceFilename);
        String filenameExt = StringUtils.getFilenameExtension(resourceFilename);

        String resourceFilename;
        if (locale.equals(Locale.ENGLISH)) {
            resourceFilename = filename + "." + filenameExt;
        } else {
            resourceFilename = filename + "_" + locale + "." + filenameExt;
        }
        return resourceFilename;
    }

    @Override
    public InputStream getResourceAsAsStream() {
        var resourceAsStream = QuestionsResourceProvider.class.getClassLoader().getResourceAsStream(getResourceFilename());
        if ( resourceAsStream == null ) {
            throw new RuntimeException("Resource not found");
        }
        return resourceAsStream;
    }

}
