package telran.java55.dto;

import lombok.Getter;
import telran.java55.model.Address;
import telran.java55.model.Farmer;
import telran.java55.model.Product;


@Getter
public class AuthorisedBagDTO {

	private Long id;
	private String name;
	private String description;
	private double price;
	private String farmName;
	private String phone;
	private String email;
	private String address;
	private Double latitude;
	private Double longitude;
	
	public AuthorisedBagDTO(Product product) {
		
		this.id = product.getId();
		this.name = product.getProductName();
		this.description = product.getDescription();
		this.price = product.getPrice();
		
		
		
		Farmer farmer = product.getFarmer();
			this.farmName = farmer.getFarmName();
			this.phone = farmer.getPhone();
			this.email = farmer.getEmail();
			this.latitude = farmer.getLocation() != null ? farmer.getLocation().getLatitude() : null;
			this.longitude = farmer.getLocation() != null ? farmer.getLocation().getLongitude() : null;
			this.address = farmer.getAddress();
			
	}
	
	
	
}
