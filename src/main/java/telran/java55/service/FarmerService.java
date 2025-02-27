package telran.java55.service;

import telran.java55.dto.LoginResponse;
import telran.java55.model.Farmer;

public interface FarmerService {
    Farmer registerFarmer(Farmer farmer);
    LoginResponse loginFarmer(String email, String password);
    String logoutFarmer();
}
