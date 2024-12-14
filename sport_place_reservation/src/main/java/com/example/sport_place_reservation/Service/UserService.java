package com.example.sport_place_reservation.Service;

import com.example.sport_place_reservation.Model.User;
import com.example.sport_place_reservation.Repository.UserRepository;
import com.example.sport_place_reservation.Util.UserIdGenerator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

  private final UserIdGenerator userIdGenerator;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserIdGenerator userIdGenerator,
                     UserRepository userRepository,
                     PasswordEncoder passwordEncoder) {
    this.userIdGenerator = userIdGenerator;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public UserDetails loadUserByUserId(String userId) {
    User user = getUserById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        new ArrayList<>()
    );
  }

  public User createUser(User user) {
    // 在事務中生成 ID 和保存用戶
    String newUserId = userIdGenerator.generateNewUserId();
    user.setUserId(newUserId);

    // 加密密碼
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);

    try {
      return userRepository.save(user);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
    }
  }

  public List<User> getAllUsers() {
    List<User> users = userRepository.findAll();
    System.out.println("Fetched Users: " + users);
    return users;
  }


  public Optional<User> getUserById(String userId) {
    return userRepository.findById(userId);
  }

  public Optional<User> getUserByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public User updateUser(String userId, User userDetails) {
    return userRepository.findById(userId)
        .map(user -> {
          user.setUsername(userDetails.getUsername());
          // 如果更新密碼，也需要加密
          if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
          }
          user.setEmail(userDetails.getEmail());
          user.setPhone(userDetails.getPhone());
          user.setGender(userDetails.getGender());
          user.setBirth(userDetails.getBirth());
          return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
  }

  public void deleteUser(String userId) {
    userRepository.deleteById(userId);
  }
}