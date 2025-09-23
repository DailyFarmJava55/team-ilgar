package telran.java55.service;

import telran.java55.dto.FarmerDTO;
import telran.java55.dto.LoginResponse;
import telran.java55.dto.RegisterFarmerDTO;

public interface FarmerService {

    LoginResponse loginFarmer(String email, String password);
    String logoutFarmer(String token);
    void updateUsername(String username, String email);
    FarmerDTO registerFarmer(RegisterFarmerDTO registerDTO);
    FarmerDTO getProfileByEmail(String email);
    FarmerDTO getCurrentFarmerProfile(String email);
}
