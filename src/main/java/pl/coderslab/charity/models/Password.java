package pl.coderslab.charity.models;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Password {

    private String email;

    private String password;

    @NotEmpty(message = "{newPassword.notEmpty}")
    private String newPassword;

    private String matchingNewPassword;

}
