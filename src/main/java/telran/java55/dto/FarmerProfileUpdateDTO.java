package telran.java55.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FarmerProfileUpdateDTO {

    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Phone number is required")
    private String phonenumber;
}
