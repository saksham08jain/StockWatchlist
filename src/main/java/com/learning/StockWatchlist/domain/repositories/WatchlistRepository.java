package com.learning.StockWatchlist.domain.repositories;

import com.learning.StockWatchlist.domain.entity.WatchlistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchlistEntity, Long> {
    List<WatchlistEntity> findByCreator_UserId(Long userId);
    
    @Query("SELECT w FROM WatchlistEntity w WHERE w.creator.userId = ?1 AND LOWER(w.name) LIKE LOWER(CONCAT('%', ?2, '%'))")
    List<WatchlistEntity> findByCreator_UserIdAndNameContainingIgnoreCase(Long userId, String namePattern);
}
