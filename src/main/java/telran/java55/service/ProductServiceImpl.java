package telran.java55.service;

import org.springframework.stereotype.Service;

import telran.java55.dao.FarmerRepository;
import telran.java55.dao.ProductRepository;
import telran.java55.dto.ProductDTO;
import telran.java55.model.Farmer;
import telran.java55.model.Product;

@Service
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;
	private final FarmerRepository farmerRepository;
	
	
	public ProductServiceImpl(ProductRepository productRepository, FarmerRepository farmerRepository) {
		this.productRepository = productRepository;
		this.farmerRepository = farmerRepository;
	}
	
	@Override
	public Product addProduct(ProductDTO productDTO, Farmer farmer) {

		Product product = new Product();
		
		product.setProductName(productDTO.getProductName());
		product.setCategory(productDTO.getCategory());
		product.setDescription(productDTO.getDescription());
		product.setPrice(productDTO.getPrice());
	    product.setProductStatus(productDTO.isProductStatus());
	    product.setLocalDate(productDTO.getLocalDate());
	    product.setBestTillDate(productDTO.getBestTillDate());
	    product.setQuantity(productDTO.getQuantity());
		product.setFarmer(farmer);
		
		return productRepository.save(product);
	}


}
