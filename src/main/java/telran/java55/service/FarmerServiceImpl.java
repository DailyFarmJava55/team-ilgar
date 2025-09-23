package telran.java55.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import telran.java55.dto.FarmerDTO;
import telran.java55.dto.LoginResponse;
import telran.java55.dto.RegisterFarmerDTO;
import telran.java55.model.Farmer;
import telran.java55.model.Location;
import telran.java55.security.JwtUtil;
import telran.java55.dao.FarmerRepository;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class FarmerServiceImpl implements FarmerService {
    private final FarmerRepository farmerRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final Set<String> blacklistedTokens = new HashSet<>();

    public FarmerServiceImpl(FarmerRepository farmerRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.farmerRepository = farmerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public FarmerDTO registerFarmer(RegisterFarmerDTO registerDTO) {
        log.info("Received DTO: {}", registerDTO);

        if (farmerRepository.existsByEmail(registerDTO.getEmail())) {
            log.warn("Attempt to register farmer with existing email: {}", registerDTO.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Farmer with this email already exists");
        }

     
        Location location = null;
        if (registerDTO.getLocation() != null && !registerDTO.getLocation().isBlank()) {
            try {
                String[] coords = registerDTO.getLocation().split(",");
                double latitude = Double.parseDouble(coords[0].trim());
                double longitude = Double.parseDouble(coords[1].trim());
                location = new Location(latitude, longitude);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid location format. Expected 'lat,lon'");
            }
        }

        Farmer farmer = new Farmer();
        farmer.setEmail(registerDTO.getEmail());
        farmer.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        farmer.setFarmName(registerDTO.getFarmName());
        farmer.setPhone(registerDTO.getPhone());
        farmer.setAddress(registerDTO.getAddress());
        farmer.setLocation(location);
        farmer.setRole("FARMER");
        farmerRepository.save(farmer);

        return new FarmerDTO(farmer);
    }

    @Override
    public LoginResponse loginFarmer(String email, String password) {
        Farmer farmer = farmerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Farmer not found"));

        if (!passwordEncoder.matches(password, farmer.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(farmer.getEmail());
        return new LoginResponse(
                farmer.getUsername(),
                farmer.getEmail(),
                farmer.getRole(),
                token,
                farmer.getId()
        );
    }

    @Override
    public String logoutFarmer(String token) {
        if (token != null && !token.isBlank()) {
            blacklistedTokens.add(token);
        }
        return "Farmer logged out successfully";
    }

    @Override
    public void updateUsername(String username, String email) {
        Farmer farmer = farmerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Farmer not found"));
        farmer.setUsername(username);
        farmerRepository.save(farmer);
    }

    @Override
    public FarmerDTO getProfileByEmail(String email) {
        Farmer farmer = farmerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Farmer not found with email: " + email));

        FarmerDTO dto = new FarmerDTO();
        dto.setEmail(farmer.getEmail());
        dto.setFarmName(farmer.getFarmName());
        dto.setPhone(farmer.getPhone());
        dto.setAddress(farmer.getAddress());
        dto.setLocation(farmer.getLocation() == null ? null : farmer.getLocation().toString());
        return dto;
    }

    @Override
    public FarmerDTO getCurrentFarmerProfile(String email) {
       
        return getProfileByEmail(email);
    }
}
