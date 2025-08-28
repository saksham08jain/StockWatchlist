package com.learning.StockWatchlist.services;

import com.learning.StockWatchlist.domain.entity.StockEntity;
import com.learning.StockWatchlist.domain.entity.StockId;

import java.util.List;
import java.util.Optional;

public interface StockService {
    List<StockEntity> findAll();
    List<StockEntity> findAllActive();
    Optional<StockEntity> findById(String exchange, String ticker);
    List<StockEntity> findByExchangeAndTickerPattern(String exchange, String tickerPattern);
    List<StockEntity> findByNamePattern(String namePattern);
    List<StockEntity> findBySector(String sector);
    StockEntity save(StockEntity stock);
    StockEntity update(String exchange, String ticker, StockEntity stock);
    StockEntity partialUpdate(String exchange, String ticker, StockEntity stock);
    void softDelete(String exchange, String ticker);
    boolean exists(String exchange, String ticker);
}



