package telran.java55.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import telran.java55.model.CustomerOrder;
import telran.java55.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	
	@PostMapping("/{customerId}")
	public ResponseEntity<CustomerOrder> placeOrder(@PathVariable Long customerId) {
		try {
			CustomerOrder order = orderService.placeOrder(customerId);
			return ResponseEntity.ok(order);
		} catch (RuntimeException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
}
