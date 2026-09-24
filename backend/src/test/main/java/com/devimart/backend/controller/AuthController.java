package com.devimart.backend.controller;

import com.devimart.backend.entity.LoginHistory;
import com.devimart.backend.entity.User;
import com.devimart.backend.repository.LoginHistoryRepository;
import com.devimart.backend.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final LoginHistoryRepository loginHistoryRepository;

    public AuthController(
            UserRepository userRepository,
            LoginHistoryRepository loginHistoryRepository) {

        this.userRepository = userRepository;
        this.loginHistoryRepository = loginHistoryRepository;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {

        User existingUser =
                userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            throw new RuntimeException("Email already registered");
        }

        return userRepository.save(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody User loginUser) {

        User user =
                userRepository.findByEmail(loginUser.getEmail());

        if (user == null) {
            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        if (!user.getPassword().equals(loginUser.getPassword())) {
            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        LoginHistory loginHistory =
                new LoginHistory();

        loginHistory.setEmail(user.getEmail());

        loginHistory.setLoginTime(
                LocalDateTime.now()
        );

        loginHistoryRepository.save(loginHistory);

        return user;
    }
}