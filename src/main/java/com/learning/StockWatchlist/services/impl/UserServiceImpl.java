package com.learning.StockWatchlist.services.impl;

import com.learning.StockWatchlist.domain.entity.UserEntity;
import com.learning.StockWatchlist.domain.repositories.UserRepository;
import com.learning.StockWatchlist.services.UserService;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;
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
    public UserEntity partialUpdate(Long userId, UserEntity userEntity) {
        //this is probably wrong right now , will come back to this
        //the issue is my db has non nullable fields so ...
        //since this takes an userEntity and updates it hmmm

//        authorEntity.setId(id);
//
//        return authorRepository.findById(id).map(existingAuthor -> {
//            Optional.ofNullable(authorEntity.getName()).ifPresent(existingAuthor::setName);
//            Optional.ofNullable(authorEntity.getAge()).ifPresent(existingAuthor::setAge);
//            return authorRepository.save(existingAuthor);
//        }).orElseThrow(() -> new RuntimeException("Author does not exist"));

        //its actual patch not overwirting save
        //i do need an id to check if the userexists and actaully update them in db
        return new UserEntity(99999L,"placeholder","placeholder","placeholder");
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
