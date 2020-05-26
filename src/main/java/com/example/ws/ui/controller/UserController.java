package com.example.ws.ui.controller;

import com.example.ws.exceptions.UserServiceException;
import com.example.ws.service.AddressesService;
import com.example.ws.service.UserService;
import com.example.ws.shared.dto.AddressDto;
import com.example.ws.shared.dto.UserDto;
import com.example.ws.ui.model.request.UserDetailsRequestModel;
import com.example.ws.ui.model.response.AddressRest;
import com.example.ws.ui.model.response.OperationStatusModel;
import com.example.ws.ui.model.response.RequestIterationName;
import com.example.ws.ui.model.response.RequestIterationStatus;
import com.example.ws.ui.model.response.UserRest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(path = "/users")
/*
produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json"},
        consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
 */
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressesService addressesService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping(path = "/{id}")
    public UserRest getUser(@PathVariable String id) {
        UserDto userFromDB = userService.getUserByUserId(id);
        return modelMapper.map(userFromDB, UserRest.class);
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws UserServiceException {

        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        UserRest returnValue = modelMapper.map(createdUser, UserRest.class);

        return returnValue;
    }

    @PutMapping(value = "/{UserId}")
    public UserRest updateUser(@PathVariable String UserId, @RequestBody UserDetailsRequestModel userDetails) {
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.updateUser(UserId, userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @DeleteMapping(value = "/{userId}")
    public OperationStatusModel deleteUser(@PathVariable String userId) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestIterationName.DELETE.name());
        returnValue.setOperationResult(RequestIterationStatus.SUCCESS.name());

        userService.deleteUser(userId);

        return returnValue;
    }

    @GetMapping
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "1") int limit) {
        List<UserDto> users = userService.getUsers(page, limit);
        return users.stream()
                .map(user -> {
                    UserRest userRest = new UserRest();
                    BeanUtils.copyProperties(user, userRest);
                    return userRest;
                })
                .collect(toList());
    }

    @GetMapping(path = "/{id}/addresses")
    public ResponseEntity<List<AddressRest>> getUserAddresses(@PathVariable String id) {
        List<AddressDto> addressDto = addressesService.getAddresses(id);
        if (CollectionUtils.isEmpty(addressDto)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        Type listType = new TypeToken<List<AddressRest>>() {
        }.getType();
        List<AddressRest> addresses = modelMapper.map(addressDto, listType);
        for (AddressRest addressRest : addresses) {
            Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId())).withSelfRel();
            Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("User");
            addressRest.add(addressLink, userLink);
        }
        return new ResponseEntity(addresses, HttpStatus.OK);
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}")
    public ResponseEntity<AddressRest> getUserAddress(@PathVariable String userId, @PathVariable String addressId) {
        AddressDto addressDto = addressesService.getAddress(userId, addressId);
        Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUser(userId)).withRel("User");
        Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");
        AddressRest addressRest = modelMapper.map(addressDto, AddressRest.class);
        addressRest.add(addressLink, userLink, addressesLink);
        return new ResponseEntity<>(addressRest, HttpStatus.OK);
    }

    @GetMapping(path = "/email-verification")
    public OperationStatusModel getUserAddress(@RequestParam(value = "token") String token) {
        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestIterationName.VERIFY_EMAIL.name());

        boolean isVerified = userService.verifyEmailToken(token);

        returnValue.setOperationResult(RequestIterationStatus.ERROR.name());
        if(isVerified)
        {
            returnValue.setOperationResult(RequestIterationStatus.SUCCESS.name());
        }
        return returnValue;
    }

}
