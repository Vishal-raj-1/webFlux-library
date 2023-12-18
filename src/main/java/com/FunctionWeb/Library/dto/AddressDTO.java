package com.FunctionWeb.Library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AddressDTO {
    @NotBlank(message = "Address.houseNo must be present")
    private String houseNo;

    @NotBlank(message = "Address.city must be present")
    private String city;

    @NotBlank(message = "Address.state must be present")
    private String state;

    public AddressDTO(String houseNo, String city, String state) {
        this.houseNo = houseNo;
        this.city = city;
        this.state = state;
    }

    public AddressDTO(){
        this.houseNo = "";
        this.city = "";
        this.state = "";
    }
}
