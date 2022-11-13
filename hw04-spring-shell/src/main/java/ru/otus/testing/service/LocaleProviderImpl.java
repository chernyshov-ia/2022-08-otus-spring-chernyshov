package ru.otus.testing.service;

import org.springframework.stereotype.Service;
import ru.otus.testing.config.AppProps;

import java.util.Locale;

@Service
public class LocaleProviderImpl implements LocaleProvider {
    private final AppProps props;

    public LocaleProviderImpl(AppProps props) {
        this.props = props;
    }

    @Override
    public Locale getLocale() {
        return props.getLocale();
    }
}
