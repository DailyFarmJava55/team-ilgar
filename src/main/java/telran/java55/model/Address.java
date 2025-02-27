package telran.java55.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Embeddable
public class Address {
	String country;
    String city;
    String street;
    String houseNumber;
    
    
	public Address(String country, String city, String street, String houseNumber) {
		super();
		this.country = country;
		this.city = city;
		this.street = street;
		this.houseNumber = houseNumber;
	}
    
    
}
