package org.example.studentmanagement.controller;

import com.google.gson.Gson;
import org.example.studentmanagement.dto.BookDTO;
import org.example.studentmanagement.dto.ChangePasswordRequest;
import org.example.studentmanagement.dto.UpdatePersonalInfoDTO;
import org.example.studentmanagement.model.Books;
import org.example.studentmanagement.model.User;
import org.example.studentmanagement.service.BooksService;
import org.example.studentmanagement.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class UserController {

    private final BooksService booksService;

    public UserController(BooksService booksService, UserService userService) {
        this.booksService = booksService;
        this.userService = userService;
    }

    @GetMapping("/user/books")
    public ResponseEntity<List<Books>> getAllBooks() {
        return ResponseEntity.ok(booksService.getAllBooks());
    }

    private final UserService userService;

    // Update Personal Information
    @PutMapping("/user/personal-info")
    public ResponseEntity<User> updatePersonalInfo(Authentication authentication,
                                                   @RequestBody UpdatePersonalInfoDTO dto) {
        Integer userId = getUserIdFromAuthentication(authentication);
        User updatedUser = userService.updatePersonalInfo(userId, dto);
        return ResponseEntity.ok(updatedUser);
    }

    // Change Password
    @PostMapping("/user/change-password")
    public ResponseEntity<String> changePassword(Authentication authentication,
                                                 @RequestBody ChangePasswordRequest dto) {
        Integer userId = getUserIdFromAuthentication(authentication);
        userService.changePassword(userId, dto);
        return ResponseEntity.ok(new Gson().toJson("Password changed successfully"));
    }

    // Extract User ID from Authentication
    private Integer getUserIdFromAuthentication(Authentication authentication) {
        // Assuming the principal stores the user's ID
        return (Integer) authentication.getPrincipal();
    }

    @GetMapping("/user/available-books")
    public ResponseEntity<List<BookDTO>> getAvailableBooks() {
        List<BookDTO> availableBooks = booksService.getAvailableBooks();
        return ResponseEntity.ok(availableBooks);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsersWithRole(User.Role.USER);
        return ResponseEntity.ok(users);
    }
}
