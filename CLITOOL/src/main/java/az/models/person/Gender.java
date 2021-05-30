package az.models.person;


import az.models.person.Person;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "genders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Gender.getAll",
                query = "SELECT g FROM Gender g"),
})
public class Gender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @OneToMany(mappedBy = "gender", fetch = FetchType.LAZY)
    private List<Person> persons = new ArrayList<>();
}
