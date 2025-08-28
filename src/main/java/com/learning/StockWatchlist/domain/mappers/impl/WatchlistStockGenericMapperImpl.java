package com.learning.StockWatchlist.domain.mappers.impl;

import com.learning.StockWatchlist.domain.dto.WatchlistStockDto;
import com.learning.StockWatchlist.domain.entity.WatchlistStockEntity;
import com.learning.StockWatchlist.domain.mappers.GenericMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class WatchlistStockGenericMapperImpl implements GenericMapper<WatchlistStockEntity, WatchlistStockDto> {

    private final ModelMapper modelMapper;
    
    public WatchlistStockGenericMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public WatchlistStockDto mapTo(WatchlistStockEntity watchlistStockEntity) {
        return WatchlistStockDto.builder()
                .watchlistId(watchlistStockEntity.getWatchlistId())
                .stockExchange(watchlistStockEntity.getStockExchange())
                .stockTicker(watchlistStockEntity.getStockTicker())
                .addedAt(watchlistStockEntity.getAddedAt())
                .build();
    }

    @Override
    public WatchlistStockEntity mapFrom(WatchlistStockDto watchlistStockDto) {
        return WatchlistStockEntity.builder()
                .watchlistId(watchlistStockDto.getWatchlistId())
                .stockExchange(watchlistStockDto.getStockExchange())
                .stockTicker(watchlistStockDto.getStockTicker())
                .build();
    }
}
