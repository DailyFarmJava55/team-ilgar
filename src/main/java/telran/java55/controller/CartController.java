package telran.java55.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import telran.java55.dto.CartRequestDTO;
import telran.java55.model.CartItem;
import telran.java55.model.User;
import telran.java55.service.CartService;
import telran.java55.service.UserService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    private Long currentUserId(Principal principal) {
        User u = userService.getUserByEmail(principal.getName());
        return u.getId();
    }

    @GetMapping
    public List<CartItem> getMyCart(Principal principal) {
        Long userId = currentUserId(principal);
        return cartService.getCartItems(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToMyCart(@RequestBody CartRequestDTO request, Principal principal) {
        Long userId = currentUserId(principal);
        cartService.addToCart(userId, request.getProductId(), request.getQuantity());
        return ResponseEntity.ok("Item added to cart");
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFromMyCart(@RequestBody CartRequestDTO request, Principal principal) {
        Long userId = currentUserId(principal);
        cartService.removeFromCart(userId, request.getProductId());
        return ResponseEntity.ok("Item removed from cart");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearMyCart(Principal principal) {
        Long userId = currentUserId(principal);
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared");
    }


    @Deprecated
    @GetMapping("/{userId}")
    public List<CartItem> getCartItemsDeprecated(@PathVariable Long userId, Principal principal) {
        return getMyCart(principal); 
    }

    @Deprecated
    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearCartDeprecated(@PathVariable Long userId, Principal principal) {
        return clearMyCart(principal); 
    }
}
