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


    /* =========================
       REGISTER
    ========================= */

    @PostMapping("/register")
    public User register(@RequestBody User user) {

        String email =
                user.getEmail() == null
                        ? ""
                        : user.getEmail().trim();

        String accountType =
                user.getAccountType() == null
                        ? "buyer"
                        : user.getAccountType().trim().toLowerCase();


        if (email.isEmpty()) {

            throw new RuntimeException(
                    "Email is required"
            );
        }


        if (user.getPassword() == null ||
                user.getPassword().isEmpty()) {

            throw new RuntimeException(
                    "Password is required"
            );
        }


        if (!accountType.equals("buyer") &&
                !accountType.equals("seller") &&
                !accountType.equals("admin")) {

            accountType = "buyer";
        }


        User existingUser =
                userRepository.findByEmail(email);


        if (existingUser != null) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }


        user.setEmail(email);

        user.setAccountType(accountType);


        return userRepository.save(user);
    }


    /* =========================
       LOGIN
    ========================= */

    @PostMapping("/login")
    public User login(
            @RequestBody User loginUser) {

        String email =
                loginUser.getEmail() == null
                        ? ""
                        : loginUser.getEmail().trim();


        String password =
                loginUser.getPassword();


        if (email.isEmpty() ||
                password == null ||
                password.isEmpty()) {

            throw new RuntimeException(
                    "Email and password are required"
            );
        }


        User user =
                userRepository.findByEmail(email);


        if (user == null) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }


        if (user.getPassword() == null ||
                !user.getPassword().equals(password)) {

            throw new RuntimeException(
                    "Invalid email or password"
            );
        }


        if (user.getAccountType() == null ||
                user.getAccountType().trim().isEmpty()) {

            user.setAccountType("buyer");
        }


        user.setAccountType(
                user.getAccountType()
                        .trim()
                        .toLowerCase()
        );


        user =
                userRepository.save(user);


        saveLoginHistory(
                user.getEmail()
        );


        return user;
    }


    /* =========================
       SAVE LOGIN HISTORY
    ========================= */

    private void saveLoginHistory(
            String email) {

        LoginHistory loginHistory =
                new LoginHistory();


        loginHistory.setEmail(
                email
        );


        loginHistory.setLoginTime(
                LocalDateTime.now()
        );


        loginHistoryRepository.save(
                loginHistory
        );
    }
}