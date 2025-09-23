package telran.java55.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java55.dao.CartItemRepository;
import telran.java55.dao.OrderRepository;
import telran.java55.dao.ProductRepository;
import telran.java55.model.CartItem;
import telran.java55.model.CustomerOrder;
import telran.java55.model.OrderItem;
import telran.java55.model.Product;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public CustomerOrder placeOrder(Long customerId) {
        List<CartItem> cartItems = cartItemRepository.findByCustomerId(customerId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Корзина пуста");
        }

        // Создаём OrderItems и считаем общую сумму
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            Product product = cartItem.getProduct();

            return OrderItem.builder()
                    .productId(product.getId())
                    .price(product.getPrice())
                    .quantity(cartItem.getQuantity())
                    .build();
        }).collect(Collectors.toList());

        double total = orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // Создаём заказ
        CustomerOrder order = CustomerOrder.builder()
                .customerId(customerId)
                .orderDate(LocalDateTime.now())
                .totalAmount(total)
                .items(orderItems)
                .build();

        // Устанавливаем связь между order и orderItems
        orderItems.forEach(item -> item.setOrder(order));

        // Сохраняем заказ
        CustomerOrder savedOrder = orderRepository.save(order);

        // Очищаем корзину
        cartItemRepository.deleteByCustomerId(customerId);

        return savedOrder;
    }
}
