package com.learning.StockWatchlist.controllers;

import com.learning.StockWatchlist.domain.dto.UserDto;
import com.learning.StockWatchlist.domain.entity.UserEntity;
import com.learning.StockWatchlist.domain.mappers.Mapper;
import com.learning.StockWatchlist.services.UserService;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
//why am i not going through usercontroller then usercontrollerimpl? cause i dont need swapping ability?
//hmmm
@RequestMapping("/api/v1")
public class UserController {
    private UserService userService;
    private Mapper<UserEntity, UserDto> userMapper;

    public UserController(UserService userService, Mapper<UserEntity, UserDto> userMapper)
    {
        this.userMapper=userMapper;
        this.userService=userService;//spring finds and injects them thats why we had mapper impl template
        //spring will find what impl uses these two tyeps and inject it here
    }

    @GetMapping(path= "/users")
    public List<UserDto> getAllUsers()
    {
        //userService returns entities
        //i need to mapp entities to dtos
        //so why not to parallel streaming??
        List<UserEntity> userEntities=userService.findAll();
        //mapToDto

       return userEntities.stream().map(userMapper::mapTo).collect(Collectors.toList());

    }
    @GetMapping(path="/users/{id}")

    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long userId)
    {
        Optional<UserEntity> foundUser=userService.findOne(userId);

        return  foundUser.map(userEntity -> {
                    UserDto userDto=userMapper.mapTo(userEntity);
                    return new ResponseEntity<>(userDto,HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        //return a single user
        //what about object to json conversion already included in rest controller?
    }

    //i need to check what happens when i have various fields since many are non nullable
    @PostMapping(path = "/users")
    //very intersting if non unique contraint isnt met userId still autoInctrements which means first userId is getting
    //incremented then db is checking contraints
    //hmm , gotta read more about dbs i guess

    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
        UserEntity userEntity = userMapper.mapFrom(user);
        UserEntity savedUserEntity = userService.save(userEntity);
        return new ResponseEntity<>(userMapper.mapTo(savedUserEntity), HttpStatus.CREATED);
    }

    @PutMapping(path = "/users/{id}")
    public ResponseEntity<UserDto> fullUpdateUser(
            @PathVariable("id") Long userId,
            @RequestBody UserDto userDto) {

        if(!userService.isExists(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userDto.setUserId(userId);
        UserEntity userEntity = userMapper.mapFrom(userDto);
        UserEntity savedUserEntity = userService.save(userEntity);
        return new ResponseEntity<>(
                userMapper.mapTo(savedUserEntity),
                HttpStatus.OK);
    }

    @PatchMapping(path = "/users/{id}")
    //isnt implmented yet so , anyway i should get a new resposne entity
    //and db shouldnt be changed
    public ResponseEntity<UserDto> partialUpdate(
            @PathVariable("id") Long userId,
            @RequestBody UserDto userDto
    ) {
        if(!userService.isExists(userId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        UserEntity userEntity = userMapper.mapFrom(userDto);
        UserEntity updatedUser = userService.partialUpdate(userId, userEntity);
        return new ResponseEntity<>(
                userMapper.mapTo(updatedUser),
                HttpStatus.OK);
    }

    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long userId) {
        userService.delete(userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
