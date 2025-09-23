package telran.java55.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java55.dao.CartItemRepository;
import telran.java55.dao.ProductRepository;
import telran.java55.model.CartItem;
import telran.java55.model.Product;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public List<CartItem> getCartItems(Long customerId) {
        return cartItemRepository.findByCustomerId(customerId);
    }

    public void addToCart(Long customerId, Long productId, int quantity) {
        if (quantity == 0) {
            return;
        }

        CartItem existingItem = cartItemRepository
                .findByCustomerIdAndProductId(customerId, productId)
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantity;

            if (newQuantity <= 0) {
                cartItemRepository.delete(existingItem);
            } else {
                existingItem.setQuantity(newQuantity);
                cartItemRepository.save(existingItem);
            }
        } else {
            if (quantity <= 0) {
                return;
            }

            Product product = productRepository.findById(productId)
            	    .orElseThrow(() -> new RuntimeException("Product not found"));
            
            CartItem newItem = new CartItem();
            newItem.setCustomerId(customerId);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            
            cartItemRepository.save(newItem);
            
            
            	
            
        }
    }

    public void removeFromCart(Long customerId, Long productId) {
        CartItem item = cartItemRepository
                .findByCustomerIdAndProductId(customerId, productId)
                .orElse(null);

        if (item != null) {
            cartItemRepository.delete(item);
        }
    }

    public void clearCart(Long customerId) {
        cartItemRepository.deleteByCustomerId(customerId);
    }
}
