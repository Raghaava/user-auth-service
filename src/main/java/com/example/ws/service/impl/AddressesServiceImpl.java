package com.example.ws.service.impl;

import com.example.ws.io.entity.AddressEntity;
import com.example.ws.io.entity.UserEntity;
import com.example.ws.io.repository.AddressesRepository;
import com.example.ws.io.repository.UserRepository;
import com.example.ws.service.AddressesService;
import com.example.ws.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

@Service
public class AddressesServiceImpl implements AddressesService {

    @Autowired
    private AddressesRepository addressesRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<AddressDto> getAddresses(String userId) {
        UserEntity user = userRepository.findByUserId(userId);
        if(user == null)
        {
            return Collections.emptyList();
        }
        List<AddressEntity> addressEntities = addressesRepository.findByUserDetails(user);
        Type listType = new TypeToken<List<AddressDto>>() {
        }.getType();
        return modelMapper.map(addressEntities, listType);
    }

    @Override
    public AddressDto getAddress(String id, String addressId) {
        AddressEntity addressEntity = addressesRepository.findByAddressId(addressId);

        if(addressEntity == null) {
            return new AddressDto();
        }

        return modelMapper.map(addressEntity, AddressDto.class);
    }
}
