package com.learning.StockWatchlist.domain.mappers.impl;

import com.learning.StockWatchlist.domain.dto.UserDto;
import com.learning.StockWatchlist.domain.entity.UserEntity;
import com.learning.StockWatchlist.domain.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component//cause this is a bean , things depend on this implementation
public class UserMapperImpl implements Mapper<UserEntity, UserDto> {

    private ModelMapper modelMapper;
    public  UserMapperImpl(ModelMapper modelMapper)
    {
        this.modelMapper=modelMapper;

    }

    @Override
    public UserDto mapTo(UserEntity userEntity) {
        //I need to return a User Dto
        return modelMapper.map(userEntity, UserDto.class);//without model mapper id need to do this via various
                                                                //constructors and ig nullable woudl also have been an issue

    }

    @Override
    public UserEntity mapFrom(UserDto userDto) {
        return modelMapper.map(userDto,UserEntity.class);
    }
}
