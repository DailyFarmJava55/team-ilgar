package telran.java55.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import telran.java55.dao.FarmerRepository;
import telran.java55.dao.SurpriseBagRepository;
import telran.java55.model.SurpriseBag;

@Service
public class SurpriseBagService {
	@Autowired
	private SurpriseBagRepository surpriseBagRepository;
	
	@Autowired
	private FarmerRepository farmerRepository;
	
	public void deleteBag(Long id, String username) {
		SurpriseBag bag = surpriseBagRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("SurpriseBag not found"));
		
		if (!bag.getFarmer().getEmail().equals(username)) {
			throw new RuntimeException("You don't have permission to delete this bag");
		}
		
		surpriseBagRepository.delete(bag);
	}
}
