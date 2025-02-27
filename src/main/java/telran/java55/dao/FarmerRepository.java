package telran.java55.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.java55.model.Farmer;

public interface FarmerRepository extends JpaRepository<Farmer, Integer> {
	Optional<Farmer> findByEmail(String email);
}
