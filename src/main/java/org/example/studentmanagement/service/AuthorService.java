package org.example.studentmanagement.service;

import org.example.studentmanagement.model.Author;
import org.example.studentmanagement.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author addOrUpdateAuthor(Author author) {
        return authorRepository.save(author);
    }

    public void deleteAuthor(Integer id) {
        authorRepository.deleteById(id);
    }
}
