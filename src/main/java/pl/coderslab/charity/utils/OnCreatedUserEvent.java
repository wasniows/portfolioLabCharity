package pl.coderslab.charity.utils;

import lombok.Data;
import org.springframework.context.ApplicationEvent;
import pl.coderslab.charity.entities.User;

public class OnCreatedUserEvent extends ApplicationEvent {

    private final String appUrl;
    private final User user;

    public OnCreatedUserEvent(User user, String appUrl) {
        super(user);

        this.user = user;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public User getUser() {
        return user;
    }
}
