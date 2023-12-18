package com.FunctionWeb.Library.model;


import com.FunctionWeb.Library.dto.AddressDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "authorsFlux")
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    private String id;
    @NotBlank(message = "Author.name must be present")
    private String name;
    @NotNull
    private AddressDTO address;

    public Author(String id, String name){
        this.id = id;
        this.name = name;
        this.address = new AddressDTO();
    }
}
