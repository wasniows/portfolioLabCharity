package pl.coderslab.charity.entities;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int quantity;

    @ManyToMany
    private List<Category> categories;

    @OneToOne(fetch = FetchType.EAGER)
    @NotNull
    private Institution institutions;

    @Column
    private String street;

    @Column
    private String city;

    @Column
    private String zipCode;

    @Column
    private String phone;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickUpDate;

    @Column
    private LocalTime pickUpTime;

    @Column
    private String pickUpComment;
}
