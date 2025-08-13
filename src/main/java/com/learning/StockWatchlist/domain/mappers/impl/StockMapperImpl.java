package com.learning.StockWatchlist.domain.mappers.impl;

import com.learning.StockWatchlist.domain.dto.StockDto;
import com.learning.StockWatchlist.domain.entity.StockEntity;
import com.learning.StockWatchlist.domain.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component//cause this is a bean , things depend on this implementation
public class StockMapperImpl implements Mapper<StockEntity, StockDto> {

    private ModelMapper modelMapper;
    public StockMapperImpl(ModelMapper modelMapper)
    {
        this.modelMapper=modelMapper;

    }

    @Override
    public StockDto mapTo(StockEntity StockEntity) {
        //I need to return a Stock Dto
        return modelMapper.map(StockEntity, StockDto.class);//without model mapper id need to do this via various
                                                                //constructors and ig nullable woudl also have been an issue

    }

    @Override
    public StockEntity mapFrom(StockDto StockDto) {
        return modelMapper.map(StockDto,StockEntity.class);
    }
}
