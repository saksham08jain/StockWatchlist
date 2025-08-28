package com.learning.StockWatchlist.services;

import com.learning.StockWatchlist.domain.entity.WatchlistStockEntity;

import java.util.List;
import java.util.Optional;

public interface WatchlistStockService {
    List<WatchlistStockEntity> findByWatchlistId(Long watchlistId);
    List<WatchlistStockEntity> findByWatchlistIdAndTickerPattern(Long watchlistId, String tickerPattern);
    Optional<WatchlistStockEntity> findById(Long watchlistId, String stockExchange, String stockTicker);
    WatchlistStockEntity addStockToWatchlist(WatchlistStockEntity watchlistStock);
    void removeStockFromWatchlist(Long watchlistId, String stockExchange, String stockTicker);
    boolean existsInWatchlist(Long watchlistId, String stockExchange, String stockTicker);
}
