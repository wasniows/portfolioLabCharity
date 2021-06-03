package pl.coderslab.charity.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import pl.coderslab.charity.entities.Authority;
import pl.coderslab.charity.entities.User;
import pl.coderslab.charity.entities.VerificationToken;
import pl.coderslab.charity.repositories.AuthorityRepository;
import pl.coderslab.charity.repositories.UserRepository;
import pl.coderslab.charity.repositories.VerificationTokenRepository;

@Data
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public void createVerificationToken(User user, String token) {

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setEmail(user.getEmail());
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public void confirmUser(String token) {

        //retrieve token
        VerificationToken verificationToken = verificationTokenRepository.findFirstByToken(token);

        //confirm user
        User user = userRepository.findFirstByEmail(verificationToken.getEmail());
        Authority authority = new Authority(user.getEmail(), "USER");
        authorityRepository.save(authority);
        user.setAuthority(authorityRepository.findFirstByEmail(user.getEmail()));
        user.setEnabled(true);
        userRepository.save(user);

        //delete from tokens
        verificationTokenRepository.delete(verificationToken);
    }
}
