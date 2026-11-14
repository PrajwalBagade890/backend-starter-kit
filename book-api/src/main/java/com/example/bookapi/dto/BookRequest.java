package com.example.bookapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record BookRequest(
    @NotBlank(message = "Title is required") @Size(max = 160, message = "Title must be at most 160 characters") String title,
    @NotBlank(message = "Author is required") @Size(max = 120, message = "Author must be at most 120 characters") String author,
    @Pattern(regexp = "^$|^[0-9Xx-]{10,20}$", message = "ISBN must contain 10 to 20 digits, X, or hyphens") String isbn,
    @Size(max = 80, message = "Genre must be at most 80 characters") String genre,
    @Min(value = 1000, message = "Published year must be 1000 or later") @Max(value = 2100, message = "Published year must be 2100 or earlier") Integer publishedYear
) {}
