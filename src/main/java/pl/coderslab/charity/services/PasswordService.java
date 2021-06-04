package pl.coderslab.charity.services;

import pl.coderslab.charity.models.Password;

public interface PasswordService {

    void createResetToken(Password password, String token);

    void update(Password password);
}
