package telran.java55.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UserDTO {
	
	@Email(message = "The email is not correct")
	@NotBlank(message = "Email can't be empty")
	String email;
	
	@NotBlank(message = "Password can't be empty")
	@Size(min = 6, message = "The password must contain at least 6 symbols")
	String password;
	
	@NotBlank(message = "The name can't be empty")
	String username;
	
	@NotBlank(message = "The role can't be empty")
	String role;
	
}
