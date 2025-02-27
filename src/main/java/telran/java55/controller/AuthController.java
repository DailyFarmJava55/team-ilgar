package telran.java55.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import telran.java55.dto.LoginRequest;
import telran.java55.dto.LoginResponse;
import telran.java55.model.Farmer;
import telran.java55.model.User;
import telran.java55.service.FarmerService;
import telran.java55.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final UserService userService;
	private final FarmerService farmerService;
	
	public AuthController(UserService userService, FarmerService farmerService) {
		this.userService = userService;
		this.farmerService = farmerService;
	}
	
	@PostMapping("/register/user")
	public ResponseEntity<User> registerUser(@RequestBody User user) {
		User registeredUser = userService.registerUser(user);
		return ResponseEntity.ok(registeredUser);
	}
	
	@PostMapping("/register/farmer")
	public ResponseEntity<Farmer> registerFarmer(@RequestBody Farmer farmer) {
		Farmer registeredFarmer = farmerService.registerFarmer(farmer);
		return ResponseEntity.ok(registeredFarmer);
	}
	
	@PostMapping("/user/login")
	public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
	    LoginResponse response = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
	    return ResponseEntity.ok(response);
	}

	@PostMapping("/farmer/login")
	public ResponseEntity<LoginResponse> loginFarmer(@RequestBody LoginRequest loginRequest) {
	    LoginResponse response = farmerService.loginFarmer(loginRequest.getEmail(), loginRequest.getPassword());
	    return ResponseEntity.ok(response);
	}

	@PostMapping("/logout/user")
    public ResponseEntity<String> logoutUser() {
        return ResponseEntity.ok(userService.logoutUser());
    }

    @PostMapping("/logout/farmer")
    public ResponseEntity<String> logoutFarmer() {
        return ResponseEntity.ok(farmerService.logoutFarmer());
    }
}
