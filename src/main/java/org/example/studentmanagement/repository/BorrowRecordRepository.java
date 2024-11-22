package org.example.studentmanagement.repository;

import org.example.studentmanagement.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {
    List<BorrowRecord> findByBookId(Integer bookId);

    List<BorrowRecord> findByStatus(String status);
}
