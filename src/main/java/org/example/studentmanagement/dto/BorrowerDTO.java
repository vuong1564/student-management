package org.example.studentmanagement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowerDTO {
    private Integer id;
    private String name;
    private String email;
    private List<BorrowedBookDTO> borrowedBooks;

}
