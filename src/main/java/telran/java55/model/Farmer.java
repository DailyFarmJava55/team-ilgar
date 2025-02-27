package telran.java55.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Farmer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	@Column(unique = true, nullable = false)
	String email;
	@Column(nullable = false)
	String password;
	@Column(nullable = false)
	String farmName;
	@Column(nullable = false)
	String username;
	@Column(nullable = false)
	String role;
	@Embedded
	Address address;
	@Embedded
	Location location;
	
}
