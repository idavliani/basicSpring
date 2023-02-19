package dln.ioanna.basicspring;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "testDB")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String firstName;
    private String lastName;
    private String country;
    private String address;
    private String phone;
    private int age;


}
