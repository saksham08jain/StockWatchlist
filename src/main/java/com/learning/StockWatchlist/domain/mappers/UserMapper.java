package com.learning.StockWatchlist.domain.mappers;

import com.learning.StockWatchlist.domain.dto.UserRequestDto;
import com.learning.StockWatchlist.domain.dto.UserResponseDto;
import com.learning.StockWatchlist.domain.entity.UserEntity;

public interface UserMapper {
    UserEntity toEntity(UserRequestDto userRequestDto) ;
    UserResponseDto toResponse(UserEntity userEntity);
}
