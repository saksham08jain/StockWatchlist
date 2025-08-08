package com.learning.StockWatchlist.services;

import com.learning.StockWatchlist.domain.entity.UserEntity;
import org.apache.catalina.User;

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
    UserEntity partialUpdate(Long userId,UserEntity userEntity);
    void delete(Long userId);
    boolean isExists(Long userId);
    UserEntity save(UserEntity u);


}
