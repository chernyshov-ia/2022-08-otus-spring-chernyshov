package ru.otus.testing.controllers;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.testing.app.Application;
import ru.otus.testing.config.AppProps;
import ru.otus.testing.context.UserContext;
import ru.otus.testing.service.IOService;
import ru.otus.testing.service.LocalizedMessageService;

@ShellComponent
public class ShellController {
    private final Application application;
    private final IOService ioService;
    private final LocalizedMessageService messageService;
    private final AppProps props;
    private final UserContext userContext;

    public ShellController(Application application, IOService ioService, LocalizedMessageService messageService,
                           AppProps props, UserContext userContext) {
        this.application = application;
        this.ioService = ioService;
        this.messageService = messageService;
        this.props = props;
        this.userContext = userContext;
    }

    @ShellMethodAvailability({"start"})
    public Availability isUsernameSelected() {
        if( userContext.getUsername() == null || "".equals(userContext.getUsername().trim())) {
            return Availability.unavailable("Username must be specified");
        } else {
            return Availability.available();
        }
    }

    @ShellMethod(value = "Specify you name", key = {"login","name", "l"})
    public void login() {
        var prompt = messageService.getMessage("app.queryName");
        var username = ioService.readStringWithPrompt(prompt + ": ");
        userContext.setUsername(username);
        ioService.outputString("You name now is " + username);
    }

    @ShellMethod(value = "Start testing", key = {"start", "run", "execute", "s"})
    public void start() {
        ioService.outputString("\"start\" command has been given to start testing.");
        application.run();
    }

}
