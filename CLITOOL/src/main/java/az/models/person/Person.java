package az.models.person;

import az.models.movie.Movie;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@Entity
@Table(name = "persons")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Persons.getAll",
                query = "SELECT p FROM Person p"),
        @NamedQuery(name = "Persons.findByName",
                query = "SELECT p FROM Person p WHERE p.name  LIKE  CONCAT(:name,'%')"),
        @NamedQuery(name = "Persons.findByProfession",
                query = "SELECT p FROM Person p WHERE p.profession.name LIKE  CONCAT(:name,'%')")

})
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private  String name;
    @NonNull
    private String surname;
    @NonNull
    private String lastname;
    @NonNull
    private LocalDate birthdate;

    @NonNull
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name="gender_id")
    private Gender gender;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="detail_id")
    private PersonDetail person_detail;

    @NonNull
    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name="profession_id")
    private Profession profession;
    @ManyToMany(mappedBy = "persons")
    private List<Movie> movies;

}