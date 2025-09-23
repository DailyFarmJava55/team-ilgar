package telran.java55.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import telran.java55.dto.FarmerDTO;
import telran.java55.dto.FarmerProfileUpdateDTO;
import telran.java55.dto.LoginRequest;
import telran.java55.dto.LoginResponse;
import telran.java55.dto.RegisterFarmerDTO;
import telran.java55.dto.UserDTO;
import telran.java55.model.User;
import telran.java55.service.FarmerService;
import telran.java55.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final FarmerService farmerService;

    public AuthController(UserService userService, FarmerService farmerService) {
        this.userService = userService;
        this.farmerService = farmerService;
    }

    private record ApiError(String code, String message) {}
    private static ResponseEntity<ApiError> err(HttpStatus status, String code, String msg) {
        return ResponseEntity.status(status).body(new ApiError(code, msg));
    }

    private static final String ERR_INVALID = "AUTH_INVALID_CREDENTIALS";
    private static final String ERR_LOCKED = "AUTH_ACCOUNT_LOCKED";
    private static final String ERR_DISABLED = "AUTH_ACCOUNT_DISABLED";
    private static final String ERR_INTERNAL = "INTERNAL_ERROR";

    // --- USER ---

    @PostMapping("/user/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        User registeredUser = userService.registerUser(userDTO);
        UserDTO dto = new UserDTO();
        dto.setEmail(registeredUser.getEmail());
        dto.setUsername(registeredUser.getUsername());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response =
                userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return err(HttpStatus.UNAUTHORIZED, ERR_INVALID, "Login or password is not correct");
        } catch (LockedException e) {
            return err(HttpStatus.LOCKED, ERR_LOCKED, "Account is temporarily locked");
        } catch (DisabledException e) {
            return err(HttpStatus.FORBIDDEN, ERR_DISABLED, "Account is disabled");
        } catch (Exception e) {
            log.error("Unexpected error on /user/login", e);
            return err(HttpStatus.INTERNAL_SERVER_ERROR, ERR_INTERNAL, "Unexpected server error");
        }
    }

    @PostMapping("/user/logout")
    public ResponseEntity<Void> logoutUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = extractBearer(authHeader);
        if (token == null) return ResponseEntity.noContent().build();
        userService.logoutUser(token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/profile")
    public ResponseEntity<UserDTO> getUserProfile(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        UserDTO dto = new UserDTO();
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        return ResponseEntity.ok(dto);
    }

    // --- FARMER ---

    @PostMapping("/farmer/register")
    public ResponseEntity<FarmerDTO> registerFarmer(
            @Valid @RequestBody RegisterFarmerDTO registerDTO) {
        log.info("RECEIVED REGISTRATION DTO: {}", registerDTO.getEmail());
        FarmerDTO registered = farmerService.registerFarmer(registerDTO);
        return ResponseEntity.ok(registered);
    }

    @PostMapping("/farmer/login")
    public ResponseEntity<?> loginFarmer(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response =
                farmerService.loginFarmer(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return err(HttpStatus.UNAUTHORIZED, ERR_INVALID, "Login or password is not correct");
        } catch (LockedException e) {
            return err(HttpStatus.LOCKED, ERR_LOCKED, "Account is temporarily locked");
        } catch (DisabledException e) {
            return err(HttpStatus.FORBIDDEN, ERR_DISABLED, "Account is disabled");
        } catch (Exception e) {
            log.error("Unexpected error on /farmer/login", e);
            return err(HttpStatus.INTERNAL_SERVER_ERROR, ERR_INTERNAL, "Unexpected server error");
        }
    }

    @PostMapping("/farmer/logout")
    public ResponseEntity<Void> logoutFarmer(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = extractBearer(authHeader);
        if (token == null) return ResponseEntity.noContent().build();
        farmerService.logoutFarmer(token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/farmer/profile")
    public ResponseEntity<FarmerDTO> getFarmerProfile(Principal principal) {
        FarmerDTO dto = farmerService.getCurrentFarmerProfile(principal.getName());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/farmer/profile/username")
    public ResponseEntity<String> updateUsername(
            @Valid @RequestBody FarmerProfileUpdateDTO dto, Principal principal) {
        farmerService.updateUsername(dto.getUsername(), principal.getName());
        return ResponseEntity.ok("Username updated");
    }

    // --- Utils ---
    private String extractBearer(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) return null;
        String prefix = "Bearer ";
        return authHeader.startsWith(prefix)
                ? authHeader.substring(prefix.length()).trim()
                : authHeader.trim();
    }
}
