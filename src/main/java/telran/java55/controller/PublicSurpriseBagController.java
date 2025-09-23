package telran.java55.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.java55.dao.SurpriseBagRepository;
import telran.java55.dto.PublicSurpriseBagDTO;
import telran.java55.model.SurpriseBag;

@RestController
@RequestMapping("/api/public")
public class PublicSurpriseBagController {
	
	private final SurpriseBagRepository surpriseBagRepository;
	
	public PublicSurpriseBagController(SurpriseBagRepository surpriseBagRepository) {
		this.surpriseBagRepository = surpriseBagRepository;
	}
	
	@GetMapping("/surprise-bags")
	public ResponseEntity<List<PublicSurpriseBagDTO>> getAllSurpriseBags() {
		List<SurpriseBag> bags = surpriseBagRepository.findAllByExpirationAfterOrderByCreatedDateDesc(LocalDate.now());
		
		List<PublicSurpriseBagDTO> dtos = bags.stream()
				.map(bag -> new PublicSurpriseBagDTO(
						bag.getId(),
						bag.getTitle(),
						bag.getDescription(),
						bag.getPrice(),
						bag.getImagePath(),
						bag.getFarmer().getFarmName()
				))
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(dtos);
	}
}
