package org.example.studentmanagement.service;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.dto.BorrowedBookDTO;
import org.example.studentmanagement.dto.BorrowerDTO;
import org.example.studentmanagement.model.BorrowRecord;
import org.example.studentmanagement.model.User;
import org.example.studentmanagement.repository.BooksRepository;
import org.example.studentmanagement.repository.BorrowRecordRepository;
import org.example.studentmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowerService {
    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final BooksRepository bookRepository;


    public List<BorrowerDTO> getAllBorrowers() {
        // Lấy danh sách người dùng có vai trò là "USER"
        List<User> users = userRepository.findByRole(User.Role.USER);
        List<BorrowerDTO> borrowerDTOs = new ArrayList<>();

        for (User user : users) {
            BorrowerDTO dto = new BorrowerDTO();
            dto.setId(user.getId());
            dto.setName(user.getUsername());
            dto.setEmail(user.getEmail());

            // Lấy danh sách sách mà người dùng này đã mượn
            List<BorrowRecord> borrowRecords = borrowRecordRepository.findByUserId(user.getId());
            List<BorrowedBookDTO> borrowedBooks = new ArrayList<>();

            for (BorrowRecord record : borrowRecords) {
                BorrowedBookDTO bookDTO = new BorrowedBookDTO();
                bookDTO.setBookId(record.getBookId());
                bookDTO.setBorrowDate(record.getBorrowDate());
                bookDTO.setReturnDate(record.getReturnDate());

                bookRepository.findById(record.getBookId()).ifPresent(book -> bookDTO.setBookTitle(book.getTitle()));
                borrowedBooks.add(bookDTO);
            }

            dto.setBorrowedBooks(borrowedBooks);
            borrowerDTOs.add(dto);
        }

        return borrowerDTOs;
    }
}

