package com.learning.StockWatchlist.services.impl;

import com.learning.StockWatchlist.domain.entity.WatchlistStockEntity;
import com.learning.StockWatchlist.domain.entity.WatchlistStockId;
import com.learning.StockWatchlist.domain.repositories.WatchlistStockRepository;
import com.learning.StockWatchlist.services.WatchlistService;
import com.learning.StockWatchlist.services.WatchlistStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WatchlistStockServiceImpl implements WatchlistStockService {

    private final WatchlistStockRepository watchlistStockRepository;
    private final WatchlistService watchlistService;

    public WatchlistStockServiceImpl(WatchlistStockRepository watchlistStockRepository, WatchlistService watchlistService) {
        this.watchlistStockRepository = watchlistStockRepository;
        this.watchlistService = watchlistService;
    }

    @Override
    public List<WatchlistStockEntity> findByWatchlistId(Long watchlistId) {
        return watchlistStockRepository.findByWatchlistId(watchlistId);
    }

    @Override
    public List<WatchlistStockEntity> findByWatchlistIdAndTickerPattern(Long watchlistId, String tickerPattern) {
        return watchlistStockRepository.findByWatchlistIdAndStockTickerContaining(watchlistId, tickerPattern);
    }

    @Override
    public Optional<WatchlistStockEntity> findById(Long watchlistId, String stockExchange, String stockTicker) {
        return watchlistStockRepository.findById(new WatchlistStockId(watchlistId, stockExchange, stockTicker));
    }

    @Override
    @Transactional
    public WatchlistStockEntity addStockToWatchlist(WatchlistStockEntity watchlistStock) {
        // Mark the watchlist for refresh when a stock is added
        watchlistService.markForRefresh(watchlistStock.getWatchlistId());
        return watchlistStockRepository.save(watchlistStock);
    }

    @Override
    @Transactional
    public void removeStockFromWatchlist(Long watchlistId, String stockExchange, String stockTicker) {
        watchlistStockRepository.deleteByWatchlistIdAndStockExchangeAndStockTicker(watchlistId, stockExchange, stockTicker);
        // Mark the watchlist for refresh when a stock is removed
        watchlistService.markForRefresh(watchlistId);
    }

    @Override
    public boolean existsInWatchlist(Long watchlistId, String stockExchange, String stockTicker) {
        return watchlistStockRepository.existsById(new WatchlistStockId(watchlistId, stockExchange, stockTicker));
    }
}
