package com.example.ws.io.repository;

import com.example.ws.io.entity.AddressEntity;
import com.example.ws.io.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressesRepository extends JpaRepository<AddressEntity, Long> {
    List<AddressEntity> findByUserDetails(UserEntity user);

    AddressEntity findByAddressId(String addressId);
}
