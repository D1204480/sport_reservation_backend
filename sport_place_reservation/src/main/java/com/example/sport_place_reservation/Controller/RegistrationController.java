package com.example.sport_place_reservation.Controller;

import com.example.sport_place_reservation.DTO.request.EmailRequest;
import com.example.sport_place_reservation.DTO.request.VerificationRequest;
import com.example.sport_place_reservation.DTO.response.VerificationResponse;
import com.example.sport_place_reservation.Repository.VerificationCodeRepository;
import com.example.sport_place_reservation.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api")
public class RegistrationController {

  @Autowired
  private EmailService emailService;

  @Autowired
  private VerificationCodeRepository verificationCodeRepository;

  @PostMapping("/send-verification-code")
  public ResponseEntity<String> sendVerificationCode(@RequestBody EmailRequest emailRequest) {
    String verificationCode = emailService.sendVerificationCode(emailRequest.getEmail());
    return ResponseEntity.ok(verificationCode);
  }

  @PostMapping("/verify-code")
  public ResponseEntity<VerificationResponse> verifyCode(@RequestBody VerificationRequest verificationRequest) {
    boolean isValid = verificationCodeRepository.isCodeValid(verificationRequest.getEmail(), verificationRequest.getCode());
    return ResponseEntity.ok(new VerificationResponse(isValid));
  }
}
