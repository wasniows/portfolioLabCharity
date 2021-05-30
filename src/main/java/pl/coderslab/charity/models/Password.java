package pl.coderslab.charity.models;

import lombok.Data;

@Data
public class Password {

    private String email;

    private String password;

    private String newPassword;

    private String matchingNewPassword;

}
