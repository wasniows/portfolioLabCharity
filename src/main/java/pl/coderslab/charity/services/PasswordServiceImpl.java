package pl.coderslab.charity.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.entities.ResetToken;
import pl.coderslab.charity.entities.User;
import pl.coderslab.charity.models.Password;
import pl.coderslab.charity.repositories.ResetTokenRepository;
import pl.coderslab.charity.repositories.UserRepository;

@Data
@Service
public class PasswordServiceImpl implements PasswordService{

    private final ResetTokenRepository resetTokenRepository;
    private final UserRepository userRepository;

    @Override
    public void createResetToken(Password password, String token) {

        ResetToken resetToken = new ResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(password.getEmail());
        resetTokenRepository.save(resetToken);
    }

    @Override
    public void update(Password password) {
        User user = userRepository.findFirstByEmail(password.getEmail());
        user.setPassword(password.getNewPassword());
        userRepository.save(user);

        //delete reset token
        ResetToken resetToken = resetTokenRepository.findFirstByToken(password.getToken());
        resetTokenRepository.delete(resetToken);
    }
}
