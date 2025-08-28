package com.learning.StockWatchlist.domain.mappers.impl;

import com.learning.StockWatchlist.domain.dto.UserRequestDto;
import com.learning.StockWatchlist.domain.dto.UserResponseDto;
import com.learning.StockWatchlist.domain.entity.UserEntity;
import com.learning.StockWatchlist.domain.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class UserMapperImpl implements UserMapper {
    private final ModelMapper modelMapper;
    @Override
    public UserEntity toEntity(UserRequestDto userRequestDto) {
        return modelMapper.map(userRequestDto,UserEntity.class);
    }

    @Override
    public UserResponseDto toResponse(UserEntity userEntity) {
        return modelMapper.map(userEntity,UserResponseDto.class);
    }
}
