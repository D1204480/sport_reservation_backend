package com.example.sport_place_reservation.Controller;

import com.example.sport_place_reservation.Model.User;
import com.example.sport_place_reservation.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/user")
public class UserController {

  @Autowired
  private UserService userService;

  List<User> userList = new ArrayList<>();


  @GetMapping
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @GetMapping("/{userId}")
  public User getUserById(@PathVariable String userId) {
    return userService.getUserById(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }


  @PostMapping
  public User createUser(@RequestBody User user) {
    return userService.createUser(user);
  }

  @PutMapping("/{userId}")
  public User updateUser(@PathVariable String userId, @RequestBody User userDetails) {
    return userService.updateUser(userId, userDetails);
  }

  @DeleteMapping("/{userId}")
  public void deleteUser(@PathVariable String userId) {
    userService.deleteUser(userId);
  }
}
