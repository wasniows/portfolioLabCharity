package pl.coderslab.charity.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name = "institutions")
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{institution.name.notEmpty}")
    @Column
    private String name;

    @NotEmpty(message = "{institution.desc.notEmpty}")
    @Column
    private String description;

}
