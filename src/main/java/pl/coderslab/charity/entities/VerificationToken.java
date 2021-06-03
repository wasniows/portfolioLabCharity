package pl.coderslab.charity.entities;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @Column(length = 60)
    private String token;

    @Column(length = 60)
    private String email;


}
