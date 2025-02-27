package telran.java55.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import telran.java55.dto.LoginResponse;
import telran.java55.model.Farmer;
import telran.java55.dao.FarmerRepository;

import java.util.Optional;

@Service
public class FarmerServiceImpl implements FarmerService {
    private final FarmerRepository farmerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public FarmerServiceImpl(FarmerRepository farmerRepository, BCryptPasswordEncoder passwordEncoder) {
        this.farmerRepository = farmerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Farmer registerFarmer(Farmer farmer) {
        Optional<Farmer> existingFarmer = farmerRepository.findByEmail(farmer.getEmail());
        if (existingFarmer.isPresent()) {
            throw new RuntimeException("Farmer with this email already exists");
        }

        farmer.setPassword(passwordEncoder.encode(farmer.getPassword()));
        return farmerRepository.save(farmer);
    }

    @Override
    public LoginResponse loginFarmer(String email, String password) {
        Farmer farmer = farmerRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        return new LoginResponse(farmer.getUsername(), farmer.getEmail(), farmer.getRole());
    }

    @Override
    public String logoutFarmer() {
        return "Farmer logged out successfully";
    }
}
