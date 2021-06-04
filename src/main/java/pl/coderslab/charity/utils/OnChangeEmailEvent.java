package pl.coderslab.charity.utils;

import org.springframework.context.ApplicationEvent;
import pl.coderslab.charity.entities.User;

public class OnChangeEmailEvent extends ApplicationEvent {

    private final String appUrl;
    private final User user;

    public OnChangeEmailEvent(User user, String appUrl) {
        super(user);

        this.appUrl = appUrl;
        this.user = user;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public User getUser() {
        return user;
    }
}
