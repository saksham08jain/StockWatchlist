package com.learning.StockWatchlist.services.impl;

import com.learning.StockWatchlist.domain.entity.WatchlistEntity;
import com.learning.StockWatchlist.domain.repositories.WatchlistRepository;
import com.learning.StockWatchlist.services.WatchlistService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WatchlistServiceImpl implements WatchlistService {

    private final WatchlistRepository watchlistRepository;

    public WatchlistServiceImpl(WatchlistRepository watchlistRepository) {
        this.watchlistRepository = watchlistRepository;
    }

    @Override
    public List<WatchlistEntity> findAll() {
        return watchlistRepository.findAll();
    }

    @Override
    public List<WatchlistEntity> findByUserId(Long userId) {
        return watchlistRepository.findByCreator_UserId(userId);
    }

    @Override
    public List<WatchlistEntity> findByUserIdAndNamePattern(Long userId, String namePattern) {
        return watchlistRepository.findByCreator_UserIdAndNameContainingIgnoreCase(userId, namePattern);
    }

    @Override
    public Optional<WatchlistEntity> findById(Long watchlistId) {
        return watchlistRepository.findById(watchlistId);
    }

    @Override
    public WatchlistEntity save(WatchlistEntity watchlist) {
        return watchlistRepository.save(watchlist);
    }

    @Override
    public WatchlistEntity update(Long watchlistId, WatchlistEntity watchlist) {
        if (!exists(watchlistId)) {
            throw new RuntimeException("Watchlist not found");
        }
        
        watchlist.setWatchlistId(watchlistId);
        return watchlistRepository.save(watchlist);
    }

    @Override
    public WatchlistEntity partialUpdate(Long watchlistId, WatchlistEntity watchlist) {
        return watchlistRepository.findById(watchlistId).map(existingWatchlist -> {
            if (watchlist.getName() != null) {
                existingWatchlist.setName(watchlist.getName());
            }
            if (watchlist.getCreator() != null) {
                existingWatchlist.setCreator(watchlist.getCreator());
            }
            return watchlistRepository.save(existingWatchlist);
        }).orElseThrow(() -> new RuntimeException("Watchlist not found"));
    }

    @Override
    public void delete(Long watchlistId) {
        watchlistRepository.deleteById(watchlistId);
    }

    @Override
    public boolean exists(Long watchlistId) {
        return watchlistRepository.existsById(watchlistId);
    }

    @Override
    public void markForRefresh(Long watchlistId) {
        watchlistRepository.findById(watchlistId).ifPresent(watchlist -> {
            watchlist.setNeedsRefresh(true);
            watchlistRepository.save(watchlist);
        });
    }
}
