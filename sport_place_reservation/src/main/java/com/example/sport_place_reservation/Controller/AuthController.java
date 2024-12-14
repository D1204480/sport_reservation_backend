package com.example.sport_place_reservation.Controller;

import com.example.sport_place_reservation.DTO.request.GoogleLoginRequest;
import com.example.sport_place_reservation.DTO.request.LoginRequest;
import com.example.sport_place_reservation.DTO.response.AuthResponse;
import com.example.sport_place_reservation.DTO.response.JwtAuthenticationResponse;
import com.example.sport_place_reservation.DTO.response.MessageResponse;
import com.example.sport_place_reservation.Service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}) // 允許前端訪問
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

  private final AuthService authService;


  public AuthController(AuthService authService) {
    this.authService = authService;

  }

  @PostMapping("/google-login")
  public ResponseEntity<AuthResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
    try {
      AuthResponse response = authService.handleGoogleLogin(request);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }


  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      log.info("Login attempt with email: {}", loginRequest.getEmail());
      JwtAuthenticationResponse response = authService.login(loginRequest);
      return ResponseEntity.ok(response);

    } catch (BadCredentialsException e) {
      log.warn("Login failed - bad credentials: {}", loginRequest.getEmail());
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(new MessageResponse("Email或密碼錯誤"));

    } catch (Exception e) {
      log.error("Login error: ", e);
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new MessageResponse("登入過程發生錯誤"));
    }
  }
}
