package com.FunctionWeb.Library.model;


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
    private Address address;

    public Author(String id, String name){
        this.id = id;
        this.name = name;
        this.address = new Address();
    }

    @Data
    @NoArgsConstructor
    public static class Address {
        @NotBlank(message = "Address.houseNo must be present")
        private String houseNo;

        @NotBlank(message = "Address.city must be present")
        private String city;

        @NotBlank(message = "Address.state must be present")
        private String state;

        public Address(String houseNo, String city, String state) {
            this.houseNo = houseNo;
            this.city = city;
            this.state = state;
        }
    }
}
