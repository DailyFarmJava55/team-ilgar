package telran.java55.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import telran.java55.dao.FarmerRepository;
import telran.java55.model.Farmer;

@Service
public class FarmerDetailsServiceImpl implements UserDetailsService {

	private final FarmerRepository farmerRepository;
	
	public FarmerDetailsServiceImpl(FarmerRepository farmerRepository) {
		this.farmerRepository = farmerRepository;
		
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Farmer farmer = farmerRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Farmer not found with email: " + email));
		
		return org.springframework.security.core.userdetails.User.builder()
				.username(farmer.getEmail())
				.password(farmer.getPassword())
				.roles("FARMER")
				.build();
	}
	
}
