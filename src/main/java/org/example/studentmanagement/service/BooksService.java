package org.example.studentmanagement.service;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.model.Books;
import org.example.studentmanagement.repository.AuthorRepository;
import org.example.studentmanagement.repository.BooksRepository;
import org.example.studentmanagement.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BooksService {
    private final BooksRepository booksRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

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
}
