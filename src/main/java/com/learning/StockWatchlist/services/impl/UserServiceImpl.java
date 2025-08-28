package com.learning.StockWatchlist.services.impl;

import com.learning.StockWatchlist.domain.dto.UserDto;
import com.learning.StockWatchlist.domain.dto.UserRequestDto;
import com.learning.StockWatchlist.domain.entity.UserEntity;
import com.learning.StockWatchlist.domain.repositories.UserRepository;
import com.learning.StockWatchlist.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;//should this be final? why why not?

    public UserServiceImpl(UserRepository userRepository)
    {
        this.userRepository=userRepository;
    }

    @Override
    public List<UserEntity> findAll() {
        //converting an iterable to list?
        return StreamSupport.stream(userRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
        //userRepository returns an iterable but we want a list
        //why we want a list though? an iterable should be enough?
        //also tutorial did something like spliterator stream

    }

    @Override
    public Optional<UserEntity> findOne(Long userId) {
        return userRepository.findById(userId);

    }

    @Override
    public UserEntity partialUpdate(Long userId, UserRequestDto userDto) {
        return userRepository.findById(userId).map(existingUser -> {
            // Only update fields that are provided in the request
            if (userDto.getEmail() != null) {
                existingUser.setEmail(userDto.getEmail());
            }
            if (userDto.getName() != null) {
                existingUser.setName(userDto.getName());
            }
            if (userDto.getMobileNumber() != null) {
                existingUser.setMobileNumber(userDto.getMobileNumber());
            }
            return userRepository.save(existingUser);
        }).orElseThrow(()->new RuntimeException("User does not exist "));
    }
    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public boolean isExists(Long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public UserEntity save(UserEntity userEntity) {
        if (userEntity.getUserId() != null) {
            // This is an update operation, not a new entity
            // We need to preserve the original createdAt value
            return userRepository.findById(userEntity.getUserId())
                .map(existingUser -> {
                    // Preserve creation timestamp
                    userEntity.setCreatedAt(existingUser.getCreatedAt());
                    return userRepository.save(userEntity);
                })
                .orElseThrow(() -> new RuntimeException("Cannot update non-existent user with ID: " + userEntity.getUserId()));
        } else {
            // This is a new entity
            return userRepository.save(userEntity);
        }
    }
}
