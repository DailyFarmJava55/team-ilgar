package telran.java55.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.java55.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
