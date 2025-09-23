package telran.java55.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
public class PublicSurpriseBagDTO {
	private Long id;
    private String title;
    private String description;
    private Double price;
    private String imagePath;
    private String farmName;
}
