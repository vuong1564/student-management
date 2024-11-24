package org.example.studentmanagement.service;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.dto.BookDTO;
import org.example.studentmanagement.dto.BorrowedBookDTO;
import org.example.studentmanagement.model.Books;
import org.example.studentmanagement.model.BorrowRecord;
import org.example.studentmanagement.repository.AuthorRepository;
import org.example.studentmanagement.repository.BooksRepository;
import org.example.studentmanagement.repository.BorrowRecordRepository;
import org.example.studentmanagement.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BooksService {
    private final BooksRepository booksRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }

    public Books addBook(Books book) {
        return booksRepository.save(book);
    }

    public Books updateBook(Integer id, Books updatedBook) {
        String authorName;
        String categoryName;
        if (updatedBook.getAuthorId() != null) {
            authorName = authorRepository.findById(updatedBook.getAuthorId()).get().getName();
        } else {
            authorName = null;
        }
        if (updatedBook.getCategoryId() != null) {
            categoryName = categoryRepository.findById(updatedBook.getCategoryId()).get().getName();
        } else {
            categoryName = null;
        }
        return booksRepository.findById(id).map(book -> {
            book.setTitle(updatedBook.getTitle());
            book.setPublishedYear(updatedBook.getPublishedYear());
            book.setAuthorName(authorName);
            book.setCategoryName(categoryName);
            book.setPrice(updatedBook.getPrice());
            book.setQuantity(updatedBook.getQuantity());
            return booksRepository.save(book);
        }).orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
    }

    public void deleteBook(Integer id) {
        booksRepository.deleteById(id);
    }

    public List<BookDTO> getAvailableBooks() {
        List<Books> books = booksRepository.findAll();
        List<BookDTO> availableBooks = new ArrayList<>();

        for (Books book : books) {
            int borrowedQuantity = borrowRecordRepository.countByBookIdAndReturnDateIsNull(book.getId());
            int availableQuantity = book.getQuantity() - borrowedQuantity;

            if (availableQuantity > 0) {
                BookDTO bookDTO = new BookDTO();
                bookDTO.setId(book.getId());
                bookDTO.setTitle(book.getTitle());
                bookDTO.setAuthorName(book.getAuthorName());
                bookDTO.setCategoryName(book.getCategoryName());
                bookDTO.setPublishedYear(book.getPublishedYear());
                bookDTO.setPrice(book.getPrice());
                bookDTO.setAvailableQuantity(availableQuantity);
                availableBooks.add(bookDTO);
            }
        }

        return availableBooks;
    }

    public List<BorrowedBookDTO> getBorrowedBooksByUser(Integer userId) {
        List<BorrowRecord> borrowRecords = borrowRecordRepository.findByUserIdAndReturnDateIsNull(userId);
        List<BorrowedBookDTO> borrowedBooks = new ArrayList<>();

        for (BorrowRecord record : borrowRecords) {
            BorrowedBookDTO bookDTO = new BorrowedBookDTO();
            bookDTO.setBookId(record.getBookId());
            bookDTO.setBorrowDate(record.getBorrowDate());
            bookDTO.setReturnDate(record.getReturnDate());

            booksRepository.findById(record.getBookId()).ifPresent(book -> bookDTO.setBookTitle(book.getTitle()));
            borrowedBooks.add(bookDTO);
        }

        return borrowedBooks;
    }
}
