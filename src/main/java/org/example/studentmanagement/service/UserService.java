package org.example.studentmanagement.service;

import lombok.AllArgsConstructor;
import org.example.studentmanagement.model.User;
import org.example.studentmanagement.repository.UserRepository;
import org.example.studentmanagement.security.JwtTokenUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.USER);
        userRepository.save(user);
    }

    public String login(String username, String password) {
        try {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
                return jwtTokenUtil.generateToken(username, user.get().getRole().name());
            }
            throw new RuntimeException("Invalid credentials");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}