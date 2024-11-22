package org.example.studentmanagement.controller;

import org.example.studentmanagement.model.BorrowRecord;
import org.example.studentmanagement.service.BorrowRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrow-records")
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;

    public BorrowRecordController(BorrowRecordService borrowRecordService) {
        this.borrowRecordService = borrowRecordService;
    }

    @GetMapping
    public ResponseEntity<List<BorrowRecord>> getAllBorrowRecords() {
        return ResponseEntity.ok(borrowRecordService.getAllBorrowRecords());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BorrowRecord>> getBorrowRecordsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(borrowRecordService.getBorrowRecordsByStatus(status));
    }

    @PostMapping
    public ResponseEntity<BorrowRecord> createBorrowRecord(@RequestBody BorrowRecord borrowRecord) {
        return ResponseEntity.ok(borrowRecordService.saveBorrowRecord(borrowRecord));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowRecord> updateReturnDate(@PathVariable Integer id, @RequestBody BorrowRecord updatedRecord) {
        BorrowRecord existingRecord = borrowRecordService.getAllBorrowRecords()
                .stream()
                .filter(record -> record.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Borrow record not found"));

        existingRecord.setReturnDate(updatedRecord.getReturnDate());
        existingRecord.setStatus("RETURNED");
        return ResponseEntity.ok(borrowRecordService.saveBorrowRecord(existingRecord));
    }
}
