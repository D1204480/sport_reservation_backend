package com.example.sport_place_reservation.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
  @NotBlank(message = "Email cannot be empty")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Password cannot be empty")
  private String password;
}