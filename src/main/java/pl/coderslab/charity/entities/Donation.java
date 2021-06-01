package pl.coderslab.charity.entities;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "{categories.notEmpty}")
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Category> categories;

    @OneToOne(fetch = FetchType.EAGER)
    @NotNull(message = "{institution.notNull}")
    private Institution institution;

    @NotEmpty(message = "{street.notEmpty}")
    @Column
    private String street;

    @NotEmpty(message = "{city.notEmpty}")
    @Column
    private String city;

    @NotEmpty(message = "{zipCode.notEmpty}")
    @Column
    private String zipCode;

    @NotEmpty(message = "{phone.notEmpty}")
    @Column
    private String phone;

    @NotNull(message = "{pickUpDate.notNull}")
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate pickUpDate;

    @NotNull(message = "{pickUpTime.notNull}")
    @Column
    private LocalTime pickUpTime;

    @Column
    private String pickUpComment;

    @Column
    private boolean received;

    @OneToOne
    private User user;
}
