package pl.coderslab.charity.entities;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "{email.valid}")
    @Size(max = 60, message = "{email.max}")
    @NotEmpty(message = "{email.notEmpty}")
    @Column(length = 60, unique = true)
    private String email;

    @Size(min = 3, max = 60, message = "{firstName.min.max}")
    @NotEmpty(message = "{firstName.notEmpty}")
    @Column(length = 60)
    private String firstName;

    @Size(min = 3, max = 60, message = "{lastName.min.max}")
    @NotEmpty(message = "{lastName.notEmpty}")
    @Column(length = 60)
    private String lastName;

    @NotEmpty(message = "{password.notEmpty}")
    @Column(length = 100)
    private String password;

    @Column
    private boolean enabled;

    @Transient
    private String matchingPassword;

}
