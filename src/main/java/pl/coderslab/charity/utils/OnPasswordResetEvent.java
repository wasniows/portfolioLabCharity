package pl.coderslab.charity.utils;

import org.springframework.context.ApplicationEvent;
import pl.coderslab.charity.models.Password;

public class OnPasswordResetEvent extends ApplicationEvent {

    private final String appUrl;
    private final Password password;

    public OnPasswordResetEvent(Password password, String appUrl) {
        super(password);

        this.appUrl = appUrl;
        this.password = password;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public Password getPassword() {
        return password;
    }
}
