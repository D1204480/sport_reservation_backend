package com.example.sport_place_reservation.Util;

import com.example.sport_place_reservation.Repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserIdGenerator {
  private static final String USER_ID_PREFIX = "U";
  private static final String USER_ID_FORMAT = "U%05d";
  private static final int INITIAL_ID = 1;

  private final UserRepository userRepository;

  public UserIdGenerator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public synchronized String generateNewUserId() {
    try {
      // 獲取最大的數字部分
      int nextNumber = userRepository.findMaxUserIdNumber()
          .map(maxNumber -> maxNumber + 1)
          .orElse(INITIAL_ID);

      return String.format(USER_ID_FORMAT, nextNumber);
    } catch (Exception e) {
      throw new RuntimeException("Error generating user ID", e);
    }
  }
}
