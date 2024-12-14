package com.example.sport_place_reservation.Service;

import com.example.sport_place_reservation.DTO.UserDTO;
import com.example.sport_place_reservation.DTO.request.GoogleLoginRequest;
import com.example.sport_place_reservation.DTO.request.LoginRequest;
import com.example.sport_place_reservation.DTO.response.AuthResponse;
import com.example.sport_place_reservation.DTO.response.JwtAuthenticationResponse;
import com.example.sport_place_reservation.Model.User;
import com.example.sport_place_reservation.Repository.UserRepository;
import com.example.sport_place_reservation.Security.JwtTokenProvider;
import com.example.sport_place_reservation.Util.UserIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AuthService {
  private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final UserIdGenerator userIdGenerator;


  public AuthService(
      AuthenticationManager authenticationManager,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      JwtTokenProvider jwtTokenProvider,
      UserIdGenerator userIdGenerator) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenProvider = jwtTokenProvider;
    this.userIdGenerator = userIdGenerator;
  }


  public AuthResponse handleGoogleLogin(GoogleLoginRequest request) {
    try {
      // 先檢查是否存在
      User user = userRepository.findByEmail(request.getEmail())
          .orElseGet(() -> {
            // 不存在則創建新用戶
            User newUser = new User();
            newUser.setUserId(userIdGenerator.generateNewUserId());
            newUser.setEmail(request.getEmail());
            newUser.setUsername(request.getName());
            newUser.setProvider("GOOGLE");
            // 設置其他欄位的預設值
            newUser.setPassword(null);  // 或給一個預設值
            newUser.setPhone(null);
            newUser.setGender(null);
            newUser.setBirth(null);
            return userRepository.save(newUser);
          });

      // 生成 JWT，使用已存在的用戶或新創建的用戶
      String jwt = jwtTokenProvider.generateToken(user);

      return new AuthResponse(jwt, UserDTO.fromEntity(user));

    } catch (Exception e) {
      // 添加日誌
      logger.error("Google login failed", e);
      throw new RuntimeException("Login failed");
    }
  }

  public Authentication authenticate(String username, String password) {
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password)
      );

      SecurityContextHolder.getContext().setAuthentication(authentication);
      return authentication;

    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("帳號或密碼錯誤");
    } catch (Exception e) {
      throw new RuntimeException("認證過程發生錯誤", e);
    }
  }


  public JwtAuthenticationResponse login(LoginRequest loginRequest) {
    logger.info("Attempting login for email: {}", loginRequest.getEmail());

    try {
      // 1. 使用 email 查找用戶
      User user = userRepository.findByEmail(loginRequest.getEmail())
          .orElseThrow(() -> new UsernameNotFoundException("此 Email 尚未註冊"));

      logger.debug("Found user with email: {}", user.getEmail());

      // 2. 檢查密碼
      if (user.getPassword() == null) {
        throw new BadCredentialsException("此帳號使用第三方登入，請使用相應的登入方式");
      }

      if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        throw new BadCredentialsException("Email 或密碼錯誤");
      }

      // 3. 生成 JWT
      String jwt = jwtTokenProvider.generateToken(user);

      logger.info("Login successful for email: {}", loginRequest.getEmail());

      // 4. 返回響應
      return new JwtAuthenticationResponse(
          jwt,
          "Bearer",
          user.getUserId(),
          user.getUsername(),
          user.getEmail(),
          user.getPhone()
      );

    } catch (UsernameNotFoundException e) {
      logger.error("User not found with email: {}", loginRequest.getEmail());
      throw new UsernameNotFoundException("此 Email 尚未註冊");
    } catch (BadCredentialsException e) {
      logger.error("Invalid password for email: {}", loginRequest.getEmail());
      throw new BadCredentialsException("Email 或密碼錯誤");
    } catch (Exception e) {
      logger.error("Login error for email: {}", loginRequest.getEmail(), e);
      throw new RuntimeException("登入過程發生錯誤");
    }
  }


  public JwtAuthenticationResponse generateToken(User user) {
    try {
      // 設置默認權限
      List<GrantedAuthority> authorities = Collections.singletonList(
          new SimpleGrantedAuthority("ROLE_USER")
      );

      // 生成 Token
      String jwt = jwtTokenProvider.generateToken(user);

      return new JwtAuthenticationResponse(
          jwt,
          "Bearer",
          user.getUserId(),
          user.getUsername(),
          user.getEmail(),
          user.getPhone());

    } catch (Exception e) {
      logger.error("Error generating token for user: {}", user.getUsername(), e);
      throw new RuntimeException("Token 生成失敗");
    }
  }
}
