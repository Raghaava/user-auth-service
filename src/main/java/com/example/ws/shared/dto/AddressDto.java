package com.example.ws.shared.dto;

import lombok.Data;

@Data
public class AddressDto {
    private long id;
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
    //bi-directional relationship
    private UserDto userDetails;
}
