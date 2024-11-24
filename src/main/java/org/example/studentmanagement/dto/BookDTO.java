package org.example.studentmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Integer id;
    private String title;
    private String authorName;
    private String categoryName;
    private Integer publishedYear;
    private BigDecimal price;
    private Integer availableQuantity;

    // Getters and Setters
}
