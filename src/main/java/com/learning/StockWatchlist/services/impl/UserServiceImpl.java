package com.learning.StockWatchlist.services.impl;

import com.learning.StockWatchlist.domain.dto.UserDto;
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
    public Optional<UserEntity> partialUpdate(Long userId, UserDto userDto) {
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
        });
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
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }
}
