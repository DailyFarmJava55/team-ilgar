package telran.java55.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
		
	@Column(unique = true, nullable = false)
	String email;
		
	@Column(nullable = false)
	String password;
		
	@Column(nullable = false)
	String username;
		
	@Column(nullable = false)
	String role;
	
	
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	
	
	
	
}
