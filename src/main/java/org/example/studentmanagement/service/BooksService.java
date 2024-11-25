package org.example.studentmanagement.service;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.dto.BookDTO;
import org.example.studentmanagement.dto.BorrowedBookDTO;
import org.example.studentmanagement.model.Author;
import org.example.studentmanagement.model.Books;
import org.example.studentmanagement.model.BorrowRecord;
import org.example.studentmanagement.model.Category;
import org.example.studentmanagement.repository.AuthorRepository;
import org.example.studentmanagement.repository.BooksRepository;
import org.example.studentmanagement.repository.BorrowRecordRepository;
import org.example.studentmanagement.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BooksService {
    private final BooksRepository booksRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public List<Books> getAllBooks() {
        // Lấy danh sách tất cả các sách
        List<Books> books = booksRepository.findAll();

        // Cập nhật authorName và categoryName cho sách được chỉ định
        books.forEach(book -> {
            String authorName = Optional.ofNullable(book.getAuthorId())
                    .flatMap(authorId -> authorRepository.findById(authorId))
                    .map(Author::getName)
                    .orElse(null);

            String categoryName = Optional.ofNullable(book.getCategoryId())
                    .flatMap(categoryId -> categoryRepository.findById(categoryId))
                    .map(Category::getName)
                    .orElse(null);

            // Cập nhật giá trị vào đối tượng Books
            book.setAuthorName(authorName);
            book.setCategoryName(categoryName);
        });

        return books;
    }

    public Books addBook(Books book) {
        return booksRepository.save(book);
    }

    public Books updateBook(Integer id, Books updatedBook) {

        return booksRepository.findById(id).map(book -> {
            book.setTitle(updatedBook.getTitle());
            book.setPublishedYear(updatedBook.getPublishedYear());
            book.setAuthorId(updatedBook.getAuthorId());
            book.setCategoryId(updatedBook.getCategoryId());
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
