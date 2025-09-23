package telran.java55.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.java55.dao.FarmerRepository;
import telran.java55.dto.ProductDTO;
import telran.java55.model.Farmer;
import telran.java55.model.Product;
import telran.java55.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
		
	private final ProductService productService;
	private final FarmerRepository farmerRepository;
	
	public ProductController(ProductService productService, FarmerRepository farmerRepository) {
		this.productService = productService;
		this.farmerRepository = farmerRepository;
	}
	
	@PostMapping("/add")
	public ResponseEntity<Product> addProduct(@RequestBody ProductDTO productDTO,
											Authentication authentication) {
		String farmerUsername = authentication.getName();
		Farmer farmer = farmerRepository.findByUsername(farmerUsername)
				.orElseThrow(() -> new RuntimeException("Farmer not found"));
	
		Product product = productService.addProduct(productDTO, farmer);
		return ResponseEntity.ok(product);
	}
	
}
