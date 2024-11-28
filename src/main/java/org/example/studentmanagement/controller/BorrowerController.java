package org.example.studentmanagement.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.dto.BorrowedBookDTO;
import org.example.studentmanagement.dto.BorrowerDTO;
import org.example.studentmanagement.service.BooksService;
import org.example.studentmanagement.service.BorrowerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class BorrowerController {
    private final BorrowerService borrowerService;
    private final BooksService booksService;
    private final Gson gson;

    @GetMapping("/admin/borrowers")
    public ResponseEntity<List<BorrowerDTO>> getAllBorrowers() {
        List<BorrowerDTO> borrowers = borrowerService.getAllBorrowers();
        return ResponseEntity.ok(borrowers);
    }

    // Add a new borrower
    @PostMapping("/admin/borrowers")
    public ResponseEntity<String> addBorrower(@RequestBody BorrowerDTO borrowerDTO) {
        borrowerService.addBorrower(borrowerDTO);
        return ResponseEntity.ok(gson.toJson("Borrower added successfully!"));
    }

    @PutMapping("/admin/borrowers/{borrowRecordId}/return-date")
    public ResponseEntity<String> updateReturnDate(@PathVariable Integer borrowRecordId, @RequestBody Map<String, String> body) {
        String returnDate = body.get("returnDate");
        borrowerService.updateReturnDate(borrowRecordId, LocalDate.parse(returnDate));
        return ResponseEntity.ok(gson.toJson("Return date updated successfully!"));
    }

    @GetMapping("/user/borrowed-books/{userId}")
    public ResponseEntity<List<BorrowedBookDTO>> getBorrowedBooks(@PathVariable Integer userId) {
        List<BorrowedBookDTO> borrowedBooks = booksService.getBorrowedBooksByUser(userId);
        return ResponseEntity.ok(borrowedBooks);
    }

}
