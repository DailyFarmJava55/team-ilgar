package telran.java55.service;

import jakarta.validation.Valid;
import telran.java55.dto.LoginResponse;
import telran.java55.dto.UserDTO;
import telran.java55.model.User;

public interface UserService {
    User registerUser(@Valid UserDTO userDTO);
    LoginResponse loginUser(String email, String password);
	String logoutUser(String token);
	User getUserByEmail(String email);

}
