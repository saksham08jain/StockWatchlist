package com.learning.StockWatchlist.domain.repositories;

import com.learning.StockWatchlist.domain.entity.WatchlistStockEntity;
import com.learning.StockWatchlist.domain.entity.WatchlistStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistStockRepository extends JpaRepository<WatchlistStockEntity, WatchlistStockId> {
    List<WatchlistStockEntity> findByWatchlistId(Long watchlistId);
    
    @Query("SELECT ws FROM WatchlistStockEntity ws WHERE ws.watchlistId = ?1 AND ws.stockTicker LIKE %?2%")
    List<WatchlistStockEntity> findByWatchlistIdAndStockTickerContaining(Long watchlistId, String tickerPattern);
    
    void deleteByWatchlistIdAndStockExchangeAndStockTicker(Long watchlistId, String stockExchange, String stockTicker);
}
