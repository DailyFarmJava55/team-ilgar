package telran.java55.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import telran.java55.dao.FarmerRepository;
import telran.java55.dao.SurpriseBagRepository;
import telran.java55.dto.SurpriseBagDTO;
import telran.java55.model.Farmer;
import telran.java55.model.SurpriseBag;
import telran.java55.service.SurpriseBagService;


@RestController
@RequestMapping("/api/surprisebags")
@RequiredArgsConstructor
public class SurpriseBagController {
	
	private final SurpriseBagRepository surpriseBagRepository;
	private final FarmerRepository farmerRepository;
	private final SurpriseBagService surpriseBagService;

	
	@PostMapping("/add")
	public ResponseEntity<?> createSurpriseBag(
			@RequestParam String title,
			@RequestParam String description,
			@RequestParam Double price,
			@RequestParam String expiration,
			@RequestParam String quantity,
			@RequestParam("file") MultipartFile file,
			Principal principal){
		
		String email = principal.getName();
		Farmer farmer = farmerRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Farmer not found"));
		
		if(farmer.getFarmName() == null || farmer.getPhone() == null ||
				farmer.getAddress() == null || farmer.getLocation() == null) {
			return ResponseEntity.badRequest().body("Please complete your profile before creating a Surprise Bag.");
		}
		
		
		SurpriseBag bag = new SurpriseBag();
		bag.setTitle(title);
		bag.setDescription(description);
		bag.setPrice(price);
		bag.setExpiration(LocalDate.parse(expiration));
		bag.setCreatedDate(LocalDateTime.now());
		bag.setQuantity(Integer.parseInt(quantity));
		bag.setFarmer(farmer);
		
		if (file.isEmpty()) {
		    return ResponseEntity.badRequest().body("File is missing");
		}
		
		
		try {
	        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
	        String uniqueFilename = UUID.randomUUID().toString() + "." + extension;

	        Path uploadPath = Paths.get("C:/Users/azeri/eclipse-workspace/project1_daily_farm/uploads");
	        if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	        }

	        Path filePath = uploadPath.resolve(uniqueFilename);
	        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	        bag.setImagePath("/uploads/" + uniqueFilename);
	        
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
	    }

	    SurpriseBag savedBag = surpriseBagRepository.save(bag);

	    SurpriseBagDTO responseDto = new SurpriseBagDTO(savedBag);
	    responseDto.setId(savedBag.getId());
	    responseDto.setTitle(savedBag.getTitle());
	    responseDto.setDescription(savedBag.getDescription());
	    responseDto.setPrice(savedBag.getPrice());
	    responseDto.setExpiration(savedBag.getExpiration());
	    responseDto.setQuantity(savedBag.getQuantity());
	    responseDto.setImagePath(savedBag.getImagePath());
	    responseDto.setCreatedDate(savedBag.getCreatedDate());

	    return ResponseEntity.ok(responseDto);
	}
		
	@GetMapping("/my")
	public ResponseEntity<List<SurpriseBagDTO>> getFarmerBags(Principal principal) {
		String email = principal.getName();
		Farmer farmer = farmerRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Farmer not found"));
		
		List<SurpriseBagDTO> dtoList = surpriseBagRepository.findByFarmer(farmer).stream()
	            .map(bag -> {
	                SurpriseBagDTO dto = new SurpriseBagDTO(bag);
	                dto.setId(bag.getId());
	                dto.setTitle(bag.getTitle());
	                dto.setDescription(bag.getDescription());
	                dto.setPrice(bag.getPrice());
	                dto.setExpiration(bag.getExpiration());
	                dto.setQuantity(bag.getQuantity());
	                dto.setImagePath(bag.getImagePath());
	                dto.setCreatedDate(bag.getCreatedDate());
	                return dto;
	            }).toList();

	    return ResponseEntity.ok(dtoList);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteSurpriseBag(@PathVariable Long id, Principal principal) {
		surpriseBagService.deleteBag(id, principal.getName());
		return ResponseEntity.noContent().build();
	}
	
	
}
