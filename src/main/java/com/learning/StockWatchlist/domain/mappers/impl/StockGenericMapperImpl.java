package com.learning.StockWatchlist.domain.mappers.impl;

import com.learning.StockWatchlist.domain.dto.StockDto;
import com.learning.StockWatchlist.domain.entity.StockEntity;
import com.learning.StockWatchlist.domain.mappers.GenericMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class StockGenericMapperImpl implements GenericMapper<StockEntity, StockDto> {

    private final ModelMapper modelMapper;
    
    public StockGenericMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public StockDto mapTo(StockEntity stockEntity) {
        return modelMapper.map(stockEntity, StockDto.class);
    }

    @Override
    public StockEntity mapFrom(StockDto stockDto) {
        return modelMapper.map(stockDto, StockEntity.class);
    }
}
