package telran.java55.service;

import telran.java55.dto.LoginResponse;
import telran.java55.model.User;

public interface UserService {
    User registerUser(User user);
    LoginResponse loginUser(String email, String password);
    String logoutUser();
}
