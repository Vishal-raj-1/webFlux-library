package com.FunctionWeb.Library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private String id;
    @NotBlank(message = "Book.genre must be present")
    private String genre;
    private String authorId;
    @NotNull
    @Positive(message = "Book.copiesAvailable must be positive")
    private int copiesAvailable;
}
