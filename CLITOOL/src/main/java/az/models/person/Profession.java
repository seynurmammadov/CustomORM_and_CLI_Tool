package az.models.person;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "professions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "Professions.getAll",
                query = "SELECT p FROM Profession p"),
})
public class Profession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String name;

    @Lob
    @NonNull
    private String description;

    @OneToMany(mappedBy = "profession", fetch = FetchType.LAZY)
    private List<Person> persons = new ArrayList<>();

}
