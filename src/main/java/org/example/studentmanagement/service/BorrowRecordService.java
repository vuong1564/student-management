package org.example.studentmanagement.service;

import org.example.studentmanagement.model.BorrowRecord;
import org.example.studentmanagement.repository.BorrowRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BorrowRecordService {

    private final BorrowRecordRepository borrowRecordRepository;

    public BorrowRecordService(BorrowRecordRepository borrowRecordRepository) {
        this.borrowRecordRepository = borrowRecordRepository;
    }

    public List<BorrowRecord> getAllBorrowRecords() {
        return borrowRecordRepository.findAll();
    }

    public List<BorrowRecord> getBorrowRecordsByStatus(String status) {
        return borrowRecordRepository.findByStatus(status);
    }

    public List<BorrowRecord> getBorrowRecordsByBookId(Integer bookId) {
        return borrowRecordRepository.findByBookId(bookId);
    }

    public BorrowRecord saveBorrowRecord(BorrowRecord borrowRecord) {
        return borrowRecordRepository.save(borrowRecord);
    }

    public void deleteBorrowRecord(Integer id) {
        borrowRecordRepository.deleteById(id);
    }
}
