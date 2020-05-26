package com.example.ws.service;

import com.example.ws.shared.dto.AddressDto;

import java.util.List;

public interface AddressesService {
    List<AddressDto> getAddresses(String userId);

    AddressDto getAddress(String id, String addressId);
}
