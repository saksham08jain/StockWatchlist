package com.learning.StockWatchlist.services;

import com.learning.StockWatchlist.domain.entity.WatchlistEntity;

import java.util.List;
import java.util.Optional;

public interface WatchlistService {
    List<WatchlistEntity> findAll();
    List<WatchlistEntity> findByUserId(Long userId);
    List<WatchlistEntity> findByUserIdAndNamePattern(Long userId, String namePattern);
    Optional<WatchlistEntity> findById(Long watchlistId);
    WatchlistEntity save(WatchlistEntity watchlist);
    WatchlistEntity update(Long watchlistId, WatchlistEntity watchlist);
    WatchlistEntity partialUpdate(Long watchlistId, WatchlistEntity watchlist);
    void delete(Long watchlistId);
    boolean exists(Long watchlistId);
    void markForRefresh(Long watchlistId);
}
