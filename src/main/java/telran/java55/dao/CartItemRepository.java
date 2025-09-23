package telran.java55.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import telran.java55.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCustomerId(Long customerId);
    Optional<CartItem> findByCustomerIdAndProductId(Long customerId, Long productId);
    void deleteByCustomerId(Long customerId);
}
