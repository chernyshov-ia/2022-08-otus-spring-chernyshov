package ru.otus.testing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@ConfigurationProperties("application")
public class AppProps {
    private Locale locale;

    private String filename;

    private int passThresholdPercents;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getPassThresholdPercents() {
        return passThresholdPercents;
    }

    public void setPassThresholdPercents(int passThresholdPercents) {
        this.passThresholdPercents = passThresholdPercents;
    }
}
