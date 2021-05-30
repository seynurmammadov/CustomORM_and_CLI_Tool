package az.models.movie;

import az.models.person.Person;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "movies")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Movies.getAll",
                query = "SELECT m FROM Movie m"),
        @NamedQuery(name = "Movies.findByName",
                query = "SELECT m FROM Movie m WHERE m.name  LIKE  CONCAT(:name,'%')")
})
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private int age_restriction;
    @Lob
    @NonNull
    private String description;
    @NonNull
    private LocalDate release_date;
    @NonNull
    private String  publisher_company;
    @NonNull
    private long box_office;
    @NonNull
    private String country;
    @NonNull
    @ManyToMany
    @JoinTable(name="language_movies",
            joinColumns=@JoinColumn(name="movie_id" ),
            inverseJoinColumns=@JoinColumn(name="language_id"))
    private List<Language> languages = new ArrayList<>();
    @NonNull
    @ManyToMany
    @JoinTable(name="genre_movies",
            joinColumns=@JoinColumn(name="movie_id"),
            inverseJoinColumns=@JoinColumn(name="genre_id"))
    private List<Genre> genres = new ArrayList<>();
    @NonNull
    @ManyToMany( fetch = FetchType.LAZY)
    @JoinTable(name="person_movies",
            joinColumns=@JoinColumn(name="movie_id"),
            inverseJoinColumns=@JoinColumn(name="person_id"))
    private List<Person> persons = new ArrayList<>();

}
