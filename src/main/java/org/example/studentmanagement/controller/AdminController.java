package org.example.studentmanagement.controller;

import org.example.studentmanagement.dto.BorrowerDTO;
import org.example.studentmanagement.model.Books;
import org.example.studentmanagement.service.BooksService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final BooksService booksService;

    public AdminController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<Books>> getAllBooks() {
        return ResponseEntity.ok(booksService.getAllBooks());
    }

    @PostMapping("/books")
    public ResponseEntity<Books> addBook(@RequestBody Books book) {
        return ResponseEntity.ok(booksService.addBook(book));
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Books> updateBook(@PathVariable Integer id, @RequestBody Books updatedBook) {
        return ResponseEntity.ok(booksService.updateBook(id, updatedBook));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer id) {
        booksService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }
}
