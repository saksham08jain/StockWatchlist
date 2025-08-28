package com.learning.StockWatchlist.controllers;

import com.learning.StockWatchlist.domain.dto.UserRequestDto;
import com.learning.StockWatchlist.domain.dto.UserResponseDto;
import com.learning.StockWatchlist.domain.entity.UserEntity;
import com.learning.StockWatchlist.domain.mappers.GenericMapper;
import com.learning.StockWatchlist.domain.mappers.UserMapper;
import com.learning.StockWatchlist.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController

@RequestMapping("/api/v1/users")
public class UserController {
    //all final fields are autowired via lombok required constructor
    //what happens it creates a constructor with final variables and puts explicit autowired on it

    private final UserService userService;
    private final UserMapper userMapper;


    @GetMapping
    public List<UserResponseDto> getAllUsers()
    {
        //userService returns entities
        //i need to mapp entities to dtos
        //so why not to parallel streaming??
        List<UserEntity> userEntities=userService.findAll();
        //mapToDto

       return userEntities.stream().map(userMapper::toResponse).collect(Collectors.toList());

    }
    @GetMapping(path="/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("id") Long userId)
    {
        Optional<UserEntity> foundUser=userService.findOne(userId);

        return  foundUser.map(userEntity -> {
                    UserResponseDto userDto=userMapper.toResponse(userEntity);
                    return new ResponseEntity<>(userDto,HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        //return a single user
        //what about object to json conversion already included in rest controller?
    }

    //i need to check what happens when i have various fields since many are non nullable
    @PostMapping
    //very intersting if non unique contraint isnt met userId still autoInctrements which means first userId is getting
    //incremented then db is checking contraints
    //hmm , gotta read more about dbs i guess

    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto user) {
        UserEntity userEntity = userMapper.toEntity(user);
        UserEntity savedUserEntity = userService.save(userEntity);
        return new ResponseEntity<>(userMapper.toResponse(savedUserEntity), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<UserResponseDto> fullUpdateUser(
            @PathVariable("id") Long userId,
            @RequestBody UserRequestDto userDto) {

        if(!userService.isExists(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userDto.setUserId(userId);
        UserEntity userEntity = userMapper.toEntity(userDto);
        UserEntity savedUserEntity = userService.save(userEntity);
        System.out.println(savedUserEntity);
        return new ResponseEntity<>(
                userMapper.toResponse(savedUserEntity),
                HttpStatus.OK);
    }



    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> partialUpdateUser(
            @PathVariable("id") Long userId,
            @RequestBody UserRequestDto incomingUser) {

        if (!userService.isExists(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserEntity updatedUser = userService.partialUpdate(userId, incomingUser);
        return new ResponseEntity<>(
                userMapper.toResponse(updatedUser),
                HttpStatus.OK);
    }
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long userId) {
        userService.delete(userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
