package telran.java55.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.java55.model.CustomerOrder;



public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
		
	}

