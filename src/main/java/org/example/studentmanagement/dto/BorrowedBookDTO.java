package org.example.studentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowedBookDTO {
    private Integer bookId;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    // Getters and Setters
}
