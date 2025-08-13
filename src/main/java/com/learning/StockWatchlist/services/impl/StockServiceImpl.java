package com.learning.StockWatchlist.services.impl;

import com.learning.StockWatchlist.domain.dto.StockDto;
import com.learning.StockWatchlist.domain.entity.StockEntity;
import com.learning.StockWatchlist.domain.repositories.StockRepository;
import com.learning.StockWatchlist.services.StockService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class StockServiceImpl implements StockService {

    private StockRepository stockRepository;//should this be final? why why not?

    public StockServiceImpl(StockRepository stockRepository)
    {
        this.stockRepository=stockRepository;
    }

    @Override
    public List<StockEntity> findAll() {
        return StreamSupport.stream(stockRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());

    }

    @Override
    public Optional<StockEntity> findOne(String stockId) {
        return stockRepository.findById(stockId);

    }

    @Override
    public StockEntity partialUpdate(String stockId, StockDto stockDto) {
        return stockRepository.findById(stockId).map(existingStock -> {
            // Only update fields that are provided in the request
            if (stockDto.getSector() != null) {
                existingStock.setSector(stockDto.getSector());
            }
            if (stockDto.getName() != null) {
                existingStock.setName(stockDto.getName());
            }
            if (stockDto.getSector() != null) {
                existingStock.setSector(stockDto.getSector());
            }
            return stockRepository.save(existingStock);
        }).orElseThrow(()->new RuntimeException("Stock does not exist "));
    }
    @Override
    public void delete(String stockId) {
        stockRepository.deleteById(stockId);
    }

    @Override
    public boolean isExists(String stockId) {
        return stockRepository.existsById(stockId);
    }

    @Override
    public StockEntity save(StockEntity stock) {
        return stockRepository.save(stock);
    }
}
