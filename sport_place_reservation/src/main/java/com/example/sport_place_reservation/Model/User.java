package com.example.sport_place_reservation.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "User")
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)  資料庫自動生成id
  @Column(name = "user_id", nullable = false, length = 50)
  private String userId;

  @Column(name = "username", nullable = false)
  private String username;

  @Column(name = "password")   // 允許為空
  private String password;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "phone")   // 允許為空
  private String phone;

  @Column(name = "gender")
  private String gender;

  @Column(name = "birth")
  @Temporal(TemporalType.DATE)
  private Date birth;

  @Column(name = "provider", nullable = false, length = 100)
  private String provider;  // GOOGLE, FACEBOOK 等

}
