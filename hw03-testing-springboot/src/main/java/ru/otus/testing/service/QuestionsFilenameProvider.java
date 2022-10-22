package ru.otus.testing.service;

import org.springframework.stereotype.Component;
import ru.otus.testing.config.AppProps;

import java.util.Locale;

@Component
public class QuestionsFilenameProvider implements ResourceFilenameProvider {
    private static String FILENAME = "questions";
    private static String FILENAME_EXTENTION = "csv";

    private AppProps props;

    public QuestionsFilenameProvider(AppProps props) {
        this.props = props;
    }

    @Override
    public String getFilename() {
        Locale locale = props.getLocale();

        if (locale.equals(Locale.ENGLISH)) {
            return FILENAME + "." + FILENAME_EXTENTION;
        }

        return FILENAME + "_" + locale + "." + FILENAME_EXTENTION;
    }
}
