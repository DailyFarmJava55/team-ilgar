package telran.java55.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SurpriseBag {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	
	@Column(length = 500)
	private String description;
	private Double price;
	private LocalDate expiration;
	private Integer quantity;
	private String imageName;
	private String imagePath;
	
	@ManyToOne
	@JoinColumn(name = "farmer_id")
	private Farmer farmer;
	
	@Column(name = "created_date")
	private LocalDateTime createdDate;
	
}
