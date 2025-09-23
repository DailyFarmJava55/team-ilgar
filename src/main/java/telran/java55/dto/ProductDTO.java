package telran.java55.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProductDTO {
	private String category;
	private String description;
	private double price;
	private boolean productStatus;
	private String productName;
	private int localDate;
	private int bestTillDate;
	private int quantity;
	private int farmerId;
	
}
