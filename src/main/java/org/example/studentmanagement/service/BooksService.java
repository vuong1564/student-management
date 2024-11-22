package org.example.studentmanagement.service;

import org.example.studentmanagement.model.Books;
import org.example.studentmanagement.repository.BooksRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksService {
    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }

    public Books addBook(Books book) {
        return booksRepository.save(book);
    }

    public Books updateBook(Integer id, Books updatedBook) {
        return booksRepository.findById(id).map(book -> {
            book.setTitle(updatedBook.getTitle());
            book.setAuthor(updatedBook.getAuthor());
            book.setPublishedYear(updatedBook.getPublishedYear());
            book.setPrice(updatedBook.getPrice());
            book.setQuantity(updatedBook.getQuantity());
            return booksRepository.save(book);
        }).orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
    }

    public void deleteBook(Integer id) {
        booksRepository.deleteById(id);
    }
}
