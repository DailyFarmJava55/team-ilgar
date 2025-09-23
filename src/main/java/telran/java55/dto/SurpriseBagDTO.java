package telran.java55.dto;


import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import telran.java55.model.Address;
import telran.java55.model.Farmer;
import telran.java55.model.SurpriseBag;

@Getter
@Setter
public class SurpriseBagDTO {
	private Long id;
	private String title;
	private String description;
	private Double price;
	private LocalDate expiration;
	private Integer quantity;
	private String imagePath;
	private LocalDateTime createdDate;
	private String farmName;
	private String phone;
	private String email;
	private String address;
	private Double latitude;
	private Double longitude;

	public SurpriseBagDTO(SurpriseBag bag) {
		this.id = bag.getId();
		this.title = bag.getTitle();
		this.description = bag.getDescription();
		this.price = bag.getPrice();
		this.expiration = bag.getExpiration();
		this.quantity = bag.getQuantity();
		this.imagePath = bag.getImagePath();
		this.createdDate = bag.getCreatedDate();

		Farmer farmer = bag.getFarmer();
		if (farmer != null) {
			this.farmName = farmer.getFarmName();
			this.phone = farmer.getPhone();
			this.email = farmer.getEmail();

			if (farmer.getLocation() != null) {
				this.latitude = farmer.getLocation().getLatitude();
				this.longitude = farmer.getLocation().getLongitude();
			}

//			Address addr = farmer.getAddress();
//			if (addr != null) {
//				this.address = String.format("%s, %s, %s, %s",
//						addr.getCountry(),
//						addr.getCity(),
//						addr.getStreet(),
//						addr.getHouseNumber());
//			}
		}
	}
}
