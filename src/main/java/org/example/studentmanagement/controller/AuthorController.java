package org.example.studentmanagement.controller;

import com.google.gson.Gson;
import org.example.studentmanagement.model.Author;
import org.example.studentmanagement.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @PostMapping
    public ResponseEntity<Author> addOrUpdateAuthor(@RequestBody Author author) {
        return ResponseEntity.ok(authorService.addOrUpdateAuthor(author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Integer id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.ok(new Gson().toJson("Author deleted successfully"));
    }
}
