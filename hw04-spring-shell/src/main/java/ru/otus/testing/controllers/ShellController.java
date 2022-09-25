package ru.otus.testing.controllers;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.testing.Application;
import ru.otus.testing.service.IOService;

@ShellComponent
public class ShellController {
    private final Application application;
    private final IOService ioService;
    private final ApplicationContext context;

    public ShellController(Application application, IOService ioService, ApplicationContext context) {
        this.application = application;
        this.ioService = ioService;
        this.context = context;
    }

    @ShellMethod(value = "Start testing command", key = {"start", "run", "execute", "s"})
    public void start() {
        ioService.outputString("\"start\" command has been given to start testing.");
        application.run();
    }

    @ShellMethod(value = "Shutdown application command", key = {"exit","quite", "shutdown", "q"})
    public void exit() {
        ioService.outputString("Shutdown");
        ((ConfigurableApplicationContext) context).close();
    }
}
