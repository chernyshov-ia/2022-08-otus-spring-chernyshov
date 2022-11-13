package ru.otus.testing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

@ConfigurationProperties("application")
public class AppProps {
    private Locale locale;

    private String resourceFilename;

    private int passThresholdPercents;

    public String getResourceFilename() {
        return resourceFilename;
    }

    public void setResourceFilename(String resourceFilename) {
        this.resourceFilename = resourceFilename;
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
