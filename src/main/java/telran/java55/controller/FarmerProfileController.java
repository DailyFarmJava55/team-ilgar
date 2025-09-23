package telran.java55.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.java55.dto.FarmerDTO;
import telran.java55.service.FarmerService;


@RestController
@RequestMapping("/api/farmer")
public class FarmerProfileController {

	private final FarmerService farmerService;
	
	public FarmerProfileController(FarmerService farmerService) {
		this.farmerService = farmerService;
	}
	
	@GetMapping("/profile")
	public ResponseEntity<FarmerDTO> getFarmerProfile(Principal principal) {
		String email = principal.getName();
		FarmerDTO farmerDTO = farmerService.getProfileByEmail(email);
		return ResponseEntity.ok(farmerDTO);
	
	}
	
}
