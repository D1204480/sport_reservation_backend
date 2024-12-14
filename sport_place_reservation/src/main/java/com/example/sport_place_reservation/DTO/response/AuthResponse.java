package com.example.sport_place_reservation.DTO.response;

import com.example.sport_place_reservation.DTO.UserDTO;

public class AuthResponse {
  private String accessToken;
  private UserDTO user;

  // 構造函數
  public AuthResponse(String accessToken, UserDTO user) {
    this.accessToken = accessToken;
    this.user = user;
  }

  // Getters and Setters
  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public UserDTO getUser() {
    return user;
  }

  public void setUser(UserDTO user) {
    this.user = user;
  }
}
