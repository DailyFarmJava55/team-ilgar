package telran.java55.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.java55.model.Farmer;
import telran.java55.model.SurpriseBag;

public interface SurpriseBagRepository extends JpaRepository<SurpriseBag, Long>{
	List<SurpriseBag> findByFarmer(Farmer farmer);
	List<SurpriseBag> findAllByExpirationAfterOrderByCreatedDateDesc(LocalDate date);
}
