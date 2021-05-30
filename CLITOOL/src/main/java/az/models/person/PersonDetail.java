package az.models.person;

import lombok.*;

import javax.persistence.*;
@Getter
@Setter
@Entity
@Table(name = "persons_details")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String address;
    @NonNull
    private String phone;
    @NonNull
    private String email;
    @OneToOne(mappedBy = "person_detail")
    private Person person;
}
