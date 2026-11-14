package com.example.bookapi.service;

import com.example.bookapi.dto.BookRequest;
import com.example.bookapi.exception.DuplicateResourceException;
import com.example.bookapi.exception.ResourceNotFoundException;
import com.example.bookapi.model.Book;
import com.example.bookapi.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class BookService {
    private final BookRepository repository;
    public BookService(BookRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    public List<Book> findAll() { return repository.findAll(); }
    @Transactional(readOnly = true)
    public Book findById(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id)); }
    public Book create(BookRequest request) {
        String isbn = normalize(request.isbn());
        if (isbn != null && repository.existsByIsbn(isbn)) throw new DuplicateResourceException("A book with this ISBN already exists");
        Book book = new Book(); apply(book, request, isbn); return repository.save(book);
    }
    public Book update(Long id, BookRequest request) {
        Book book = findById(id); String isbn = normalize(request.isbn());
        if (isbn != null && repository.findByIsbn(isbn).filter(existing -> !existing.getId().equals(id)).isPresent())
            throw new DuplicateResourceException("A book with this ISBN already exists");
        apply(book, request, isbn); return repository.save(book);
    }
    public void delete(Long id) { repository.delete(findById(id)); }
    private void apply(Book book, BookRequest request, String isbn) {
        book.setTitle(request.title().trim()); book.setAuthor(request.author().trim()); book.setIsbn(isbn);
        book.setGenre(request.genre() == null || request.genre().isBlank() ? null : request.genre().trim());
        book.setPublishedYear(request.publishedYear());
    }
    private String normalize(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
