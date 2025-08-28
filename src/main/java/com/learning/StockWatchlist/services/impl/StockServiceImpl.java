package com.learning.StockWatchlist.services.impl;

import com.learning.StockWatchlist.domain.entity.StockEntity;
import com.learning.StockWatchlist.domain.entity.StockId;
import com.learning.StockWatchlist.domain.repositories.StockRepository;
import com.learning.StockWatchlist.services.StockService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<StockEntity> findAll() {
        return stockRepository.findAll();
    }

    @Override
    public List<StockEntity> findAllActive() {
        return stockRepository.findByIsActiveTrue();
    }

    @Override
    public Optional<StockEntity> findById(String exchange, String ticker) {
        return stockRepository.findById(new StockId(exchange, ticker));
    }

    @Override
    public List<StockEntity> findByExchangeAndTickerPattern(String exchange, String tickerPattern) {
        return stockRepository.findByExchangeAndTickerContaining(exchange, tickerPattern);
    }

    @Override
    public List<StockEntity> findByNamePattern(String namePattern) {
        return stockRepository.findByNameContainingIgnoreCase(namePattern);
    }

    @Override
    public List<StockEntity> findBySector(String sector) {
        return stockRepository.findBySectorAndIsActiveTrue(sector);
    }

    @Override
    public StockEntity save(StockEntity stock) {
        return stockRepository.save(stock);
    }

    @Override
    public StockEntity update(String exchange, String ticker, StockEntity stock) {
        if (!exists(exchange, ticker)) {
            throw new RuntimeException("Stock not found");
        }
        
        stock.setExchange(exchange);
        stock.setTicker(ticker);
        return stockRepository.save(stock);
    }

    @Override
    public StockEntity partialUpdate(String exchange, String ticker, StockEntity stock) {
        return stockRepository.findById(new StockId(exchange, ticker)).map(existingStock -> {
            if (stock.getName() != null) {
                existingStock.setName(stock.getName());
            }
            if (stock.getSector() != null) {
                existingStock.setSector(stock.getSector());
            }
            if (stock.getIsActive() != null) {
                existingStock.setIsActive(stock.getIsActive());
                if (!stock.getIsActive()) {
                    existingStock.setDelistedAt(LocalDateTime.now());
                }
            }
            return stockRepository.save(existingStock);
        }).orElseThrow(() -> new RuntimeException("Stock not found"));
    }

    @Override
    public void softDelete(String exchange, String ticker) {
        stockRepository.findById(new StockId(exchange, ticker)).ifPresent(stock -> {
            stock.setIsActive(false);
            stock.setDelistedAt(LocalDateTime.now());
            stockRepository.save(stock);
        });
    }

    @Override
    public boolean exists(String exchange, String ticker) {
        return stockRepository.existsById(new StockId(exchange, ticker));
    }
}
