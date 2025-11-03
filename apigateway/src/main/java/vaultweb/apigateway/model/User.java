package vaultweb.apigateway.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
public class User {

	@Getter
	@Setter
	int id;
	@Getter
	@Setter
	String name;
	@Getter
	@Setter
	String email;
	@Getter
	@Setter
	String password;

	public User(final int id, final String name, final String email, final String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

}
