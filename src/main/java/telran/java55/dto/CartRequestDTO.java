package telran.java55.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequestDTO {

	public Long userId;
	public Long productId;
	public int quantity;
}
