package telran.java55.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import telran.java55.model.Farmer;

@Getter
@Setter
public class FarmerDTO {
	
	@Email(message = "The email is not correct")
	@NotBlank(message = "Email can't be empty")
	String email;
	
	@NotBlank(message = "Password can't be empty")
	@Size(min = 6, message = "The password must contain at least 6 symbols")
	String password;
	
	@NotBlank(message = "The Farm name can't be empty")
	String farmName;
	
	public FarmerDTO(Farmer farmer) {
        this.email = farmer.getEmail();
        this.password = farmer.getPassword();
        this.farmName = farmer.getFarmName();
        this.phone = farmer.getPhone();
        this.address = farmer.getAddress().toString();
        this.location = farmer.getLocation().toString();
        
    }

    
    public FarmerDTO() {}
    
    
    private String phone;
    private String address;
    private String location;
    
    
    
}
