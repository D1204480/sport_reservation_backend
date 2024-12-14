package com.example.sport_place_reservation.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleLoginRequest {
  private String googleToken;
  private String email;
  private String name;
  private String picture;
}
