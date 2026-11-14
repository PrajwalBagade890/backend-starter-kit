package com.example.bookapi.controller;

import com.example.bookapi.dto.BookRequest;
import com.example.bookapi.model.Book;
import com.example.bookapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Books", description = "Create, read, update and delete books")
public class BookController {
    private final BookService service;
    public BookController(BookService service) { this.service = service; }

    @GetMapping @Operation(summary = "Get all books")
    public List<Book> getAll() { return service.findAll(); }
    @GetMapping("/{id}") @Operation(summary = "Get a book by ID")
    public Book getById(@PathVariable Long id) { return service.findById(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) @Operation(summary = "Create a book")
    public Book create(@Valid @RequestBody BookRequest request) { return service.create(request); }
    @PutMapping("/{id}") @Operation(summary = "Update a book")
    public Book update(@PathVariable Long id, @Valid @RequestBody BookRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) @Operation(summary = "Delete a book")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
