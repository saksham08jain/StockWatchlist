package com.learning.StockWatchlist.domain.mappers.impl;

import com.learning.StockWatchlist.domain.dto.WatchlistDto;
import com.learning.StockWatchlist.domain.entity.WatchlistEntity;
import com.learning.StockWatchlist.domain.mappers.GenericMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class WatchlistGenericMapperImpl implements GenericMapper<WatchlistEntity, WatchlistDto> {

    private final ModelMapper modelMapper;
    
    public WatchlistGenericMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        
        // Custom mapping for creator fields
        modelMapper.createTypeMap(WatchlistEntity.class, WatchlistDto.class)
            .addMappings(mapper -> {
                mapper.map(src -> src.getWatchlistId(), WatchlistDto::setWatchlistId);
                mapper.map(src -> src.getName(), WatchlistDto::setName);
                mapper.map(src -> src.getCreator() != null ? src.getCreator().getUserId() : null, WatchlistDto::setCreatorId);
                mapper.map(src -> src.getCreator() != null ? src.getCreator().getName() : null, WatchlistDto::setCreatorName);
                mapper.map(src -> src.getNeedsRefresh(), WatchlistDto::setNeedsRefresh);
                mapper.map(src -> src.getCreatedAt(), WatchlistDto::setCreatedAt);
                mapper.map(src -> src.getUpdatedAt(), WatchlistDto::setUpdatedAt);
            });
    }

    @Override
    public WatchlistDto mapTo(WatchlistEntity watchlistEntity) {
        return modelMapper.map(watchlistEntity, WatchlistDto.class);
    }

    @Override
    public WatchlistEntity mapFrom(WatchlistDto watchlistDto) {
        return modelMapper.map(watchlistDto, WatchlistEntity.class);
    }
}
