package com.learning.StockWatchlist.domain.repositories;

import com.learning.StockWatchlist.domain.entity.StockEntity;
import com.learning.StockWatchlist.domain.entity.StockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, StockId> {
    List<StockEntity> findBySectorAndIsActiveTrue(String sector);
    List<StockEntity> findByIsActiveTrue();
    
    @Query("SELECT s FROM StockEntity s WHERE s.exchange = ?1 AND s.ticker LIKE %?2%")
    List<StockEntity> findByExchangeAndTickerContaining(String exchange, String tickerPattern);
    
    @Query("SELECT s FROM StockEntity s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<StockEntity> findByNameContainingIgnoreCase(String namePattern);
}
