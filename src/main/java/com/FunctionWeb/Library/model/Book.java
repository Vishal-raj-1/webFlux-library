package com.FunctionWeb.Library.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "booksFlux")
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private String id;
    @NotBlank(message = "Book.genre must be present")
    private String genre;
    private String authorId;
    @NotNull
    @Positive(message = "Book.copiesAvailable must be positive")
    private int copiesAvailable;
}
