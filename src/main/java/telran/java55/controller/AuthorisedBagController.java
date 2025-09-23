package telran.java55.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import telran.java55.dao.SurpriseBagRepository;
import telran.java55.dto.SurpriseBagDTO;

import telran.java55.model.SurpriseBag;

@RestController
@RequestMapping("/api")
public class AuthorisedBagController {
	
	private final SurpriseBagRepository  surpriseBagRepository;
	
	public AuthorisedBagController(SurpriseBagRepository  surpriseBagRepository) {
		
		this.surpriseBagRepository = surpriseBagRepository;
	}
	
	@GetMapping("/products-auth")
	@PreAuthorize("hasAnyRole('USER', 'CUSTOMER')")
	public ResponseEntity<List<SurpriseBagDTO>> getAllProductDetails() {
		LocalDate now = LocalDate.now();
		List<SurpriseBag> bags = surpriseBagRepository.findAllByExpirationAfterOrderByCreatedDateDesc(now);

		List<SurpriseBagDTO> dtos = bags.stream()
				.map(SurpriseBagDTO::new)
				.collect(Collectors.toList());

		return ResponseEntity.ok(dtos);
	}
	
}
