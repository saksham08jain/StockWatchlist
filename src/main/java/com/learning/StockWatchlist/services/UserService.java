package com.learning.StockWatchlist.services;

import com.learning.StockWatchlist.domain.dto.UserDto;
import com.learning.StockWatchlist.domain.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService
{
    //What do i want UserService to do?
    //we are keeping the contract and implementation separate
    //probably because its easy to switch in and out
    /*
    i want a
    findALl
    findOne
    saveOne
    deleteOne
     */
    List<UserEntity> findAll();
    Optional<UserEntity>findOne(Long userId);
    Optional<UserEntity> partialUpdate(Long userId, UserEntity userEntity);

    Optional<UserEntity> partialUpdate(Long userId, UserDto userDto);

    void delete(Long userId);
    boolean isExists(Long userId);
    UserEntity save(UserEntity u);


}
