package org.example.studentmanagement.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.studentmanagement.dto.ChangePasswordRequest;
import org.example.studentmanagement.dto.TokenResponse;
import org.example.studentmanagement.dto.UpdatePersonalInfoDTO;
import org.example.studentmanagement.dto.UserRequest;
import org.example.studentmanagement.model.User;
import org.example.studentmanagement.repository.UserRepository;
import org.example.studentmanagement.security.JwtTokenUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void registerUser(UserRequest registrationDTO) {
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists!");
        }

        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setEmail(registrationDTO.getEmail());
        user.setFullName(registrationDTO.getFullName());
        user.setAddress(registrationDTO.getAddress());
        user.setRole(User.Role.USER);

        userRepository.save(user);
    }

    public TokenResponse login(String username, String password) {
        try {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
                String token = jwtTokenUtil.generateToken(username, user.get().getRole().name());
                String role = jwtTokenUtil.getRoleFromToken(token);
                Integer userId = user.get().getId();
                return new TokenResponse(token, role, userId);
            }
            throw new RuntimeException("Invalid credentials");

        } catch (Exception e) {
            log.info(e.getMessage());
            throw e;
        }
    }

    // Change Password
    public void changePassword(Integer userId, ChangePasswordRequest dto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        // Validate old password
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setUpdatedAt(java.time.Instant.now());
        userRepository.save(user);
    }

    public User updatePersonalInfo(Integer userId, UpdatePersonalInfoDTO dto) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setAddress(dto.getAddress());

        user.setUpdatedAt(java.time.Instant.now());
        return userRepository.save(user);
    }

    public List<User> getAllUsersWithRole(User.Role role) {
        List<User> users = userRepository.findByRole(role);
        if (users.isEmpty()) {
            throw new IllegalArgumentException("Role not found: " + role);
        }
        return users; // Trả về danh sách nếu không rỗng

    }
}