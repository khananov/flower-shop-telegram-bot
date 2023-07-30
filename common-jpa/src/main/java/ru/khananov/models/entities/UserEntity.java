package ru.khananov.models.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.khananov.models.enums.Role;

@Entity
@Table(name = "security_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @ToString.Include(name = "password")
    private String maskPassword() {
        return "********";
    }
}
