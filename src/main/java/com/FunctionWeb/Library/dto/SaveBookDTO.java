package com.FunctionWeb.Library.dto;

import lombok.Data;

@Data
public class SaveBookDTO {
    private BookDTO book;
    private String authorName;
}
