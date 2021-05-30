package az.models.movie;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "genres")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Genre.getAll",
                query = "SELECT g FROM Genre g"),
})
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @ManyToMany(mappedBy = "genres")
    private List<Movie> movies;
}
