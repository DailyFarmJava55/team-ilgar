package telran.java55.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import telran.java55.dto.LoginResponse;
import telran.java55.dto.UserDTO;
import telran.java55.model.User;
import telran.java55.security.JwtUtil;
import telran.java55.dao.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final Set<String> blacklistedTokens = new HashSet<>();


    public UserServiceImpl(UserRepository userRepository, 
    		PasswordEncoder passwordEncoder,
    		JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }


    @Override
    public User registerUser(@Valid UserDTO userDTO) {
           	
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUsername(userDTO.getUsername());
        user.setRole(userDTO.getRole().toUpperCase());
        return userRepository.save(user);
    }

    @Override
    public LoginResponse loginUser(String email, String password) {
    	
        User user = userRepository.findByEmail(email)
        		.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        		
                if (!passwordEncoder.matches(password, user.getPassword())) {
                	throw new BadCredentialsException("Invalid email or password");
                }

        String token = jwtUtil.generateToken(user.getEmail());
        
        return new LoginResponse(
        		user.getUsername(), 
        		user.getEmail(), 
        		user.getRole(), 
        		token, 
        		user.getId()
        	);
    }

    @Override
    public String logoutUser(String token) {
    	blacklistedTokens.add(token);
        return "User logged out successfully";
    }
    
    public boolean isTokenBlacklisted(String token) {
    	return blacklistedTokens.contains(token);
    }

	
	@Override
	public User getUserByEmail(String email) {
	    return userRepository.findByEmail(email)
	        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}


}
