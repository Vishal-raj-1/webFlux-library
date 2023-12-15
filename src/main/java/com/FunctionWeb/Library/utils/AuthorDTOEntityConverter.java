package com.FunctionWeb.Library.utils;

import com.FunctionWeb.Library.dto.AuthorDTO;
import com.FunctionWeb.Library.model.Author;
import org.springframework.beans.BeanUtils;

public class AuthorDTOEntityConverter {
    public static AuthorDTO entityToDTO(Author author){
        AuthorDTO authorDTO = new AuthorDTO();
        BeanUtils.copyProperties(author, authorDTO);
        return authorDTO;
    }

    public static Author dtoToEntity(AuthorDTO authorDTO){
        Author author = new Author();
        BeanUtils.copyProperties(authorDTO, author);
        return author;
    }
}