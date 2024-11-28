package org.example.studentmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.dto.BorrowedBookDTO;
import org.example.studentmanagement.dto.BorrowerDTO;
import org.example.studentmanagement.service.BooksService;
import org.example.studentmanagement.service.BorrowerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class BorrowerController {
    private final BorrowerService borrowerService;
    private final BooksService booksService;

    @GetMapping("/admin/borrowers")
    public ResponseEntity<List<BorrowerDTO>> getAllBorrowers() {
        List<BorrowerDTO> borrowers = borrowerService.getAllBorrowers();
        return ResponseEntity.ok(borrowers);
    }

    // Add a new borrower
    @PostMapping("/admin/borrowers")
    public ResponseEntity<String> addBorrower(@RequestBody BorrowerDTO borrowerDTO) {
        borrowerService.addBorrower(borrowerDTO);
        return ResponseEntity.ok("Borrower added successfully!");
    }

    @GetMapping("/user/borrowed-books/{userId}")
    public ResponseEntity<List<BorrowedBookDTO>> getBorrowedBooks(@PathVariable Integer userId) {
        List<BorrowedBookDTO> borrowedBooks = booksService.getBorrowedBooksByUser(userId);
        return ResponseEntity.ok(borrowedBooks);
    }

}
