package telran.java55.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String category;
	private String description;
	private double price;
	private boolean productStatus;
	private String productName;
	private int localDate;
	private int bestTillDate;
	private int quantity;
	private String title;
	

	@ManyToOne
	@JoinColumn(name = "farmer_id")
	private Farmer farmer;
}
