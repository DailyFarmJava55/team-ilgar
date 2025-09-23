package telran.java55.service;

import telran.java55.dto.ProductDTO;
import telran.java55.model.Farmer;
import telran.java55.model.Product;

public interface ProductService {
	Product addProduct(ProductDTO productDTO, Farmer farmer);
}
