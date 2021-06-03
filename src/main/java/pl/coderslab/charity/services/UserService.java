package pl.coderslab.charity.services;

import pl.coderslab.charity.entities.User;

public interface UserService {

    User create(User user);

    void createVerificationToken(User user, String token);

    void confirmUser(String token);
}
