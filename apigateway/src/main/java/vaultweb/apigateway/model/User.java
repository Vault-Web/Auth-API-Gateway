package vaultweb.apigateway.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
@Builder
public class User {
    @Id
    private Integer id;
    private String name;
    private String username;
    private String email; // Constraints (unique, nullable) defined in database schema
    private String password;
}
