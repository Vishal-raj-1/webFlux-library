package com.FunctionWeb.Library.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class AuthorDTO {

    private String id;
    @NotBlank(message = "Author.name must be present")
    private String name;
    @NotNull
    private AddressDTO address;

    public AuthorDTO(String id, String name){
        this.id = id;
        this.name = name;
        this.address = new AddressDTO();
    }

    public AuthorDTO(){
        this.address = new AddressDTO();
    }
}
