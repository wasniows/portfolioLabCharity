package pl.coderslab.charity.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "institutions")
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    public String getDescription() {
        return description;
    }
}
