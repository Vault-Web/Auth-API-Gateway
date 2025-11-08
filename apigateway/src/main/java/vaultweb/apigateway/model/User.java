package vaultweb.apigateway.model;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class User {

    int id;
    String name;
    String email;
    String password;

    public User(
        final int id,
        final String name,
        final String email,
        final String password
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
