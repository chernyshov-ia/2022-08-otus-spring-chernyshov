package ru.otus.testing.service;

import org.springframework.util.StringUtils;

import java.util.Locale;


public class FilenameProviderImpl implements FilenameProvider {
    private final LocaleProvider localeProvider;
    private final String resourceFilename;

    public FilenameProviderImpl(LocaleProvider localeProvider, String resourceFilename) {
        this.localeProvider = localeProvider;
        this.resourceFilename = resourceFilename;
    }

    @Override
    public String getFilename() {
        var locale = localeProvider.getLocale();

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
}
