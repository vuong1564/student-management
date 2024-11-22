package org.example.studentmanagement.controller;

import org.example.studentmanagement.model.Books;
import org.example.studentmanagement.service.BooksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final BooksService booksService;

    public UserController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<Books>> getAllBooks() {
        return ResponseEntity.ok(booksService.getAllBooks());
    }
}
