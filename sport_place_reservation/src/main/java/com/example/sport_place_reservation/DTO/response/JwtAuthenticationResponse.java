package com.example.sport_place_reservation.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse {
  private String token;
  private String type;
  private String userId;
  private String username;
  private String email;
  private String phone;
}
