package org.example.studentmanagement.controller;

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
@RequestMapping("/user")
public class UserController {

    private final BooksService booksService;

    public UserController(BooksService booksService, UserService userService) {
        this.booksService = booksService;
        this.userService = userService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<Books>> getAllBooks() {
        return ResponseEntity.ok(booksService.getAllBooks());
    }

    private final UserService userService;

    // Update Personal Information
    @PutMapping("/personal-info")
    public ResponseEntity<User> updatePersonalInfo(Authentication authentication,
                                                   @RequestBody UpdatePersonalInfoDTO dto) {
        Integer userId = getUserIdFromAuthentication(authentication);
        User updatedUser = userService.updatePersonalInfo(userId, dto);
        return ResponseEntity.ok(updatedUser);
    }

    // Change Password
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(Authentication authentication,
                                                 @RequestBody ChangePasswordRequest dto) {
        Integer userId = getUserIdFromAuthentication(authentication);
        userService.changePassword(userId, dto);
        return ResponseEntity.ok("Password changed successfully");
    }

    // Extract User ID from Authentication
    private Integer getUserIdFromAuthentication(Authentication authentication) {
        // Assuming the principal stores the user's ID
        return (Integer) authentication.getPrincipal();
    }

    @GetMapping("/available-books")
    public ResponseEntity<List<BookDTO>> getAvailableBooks() {
        List<BookDTO> availableBooks = booksService.getAvailableBooks();
        return ResponseEntity.ok(availableBooks);
    }
}
