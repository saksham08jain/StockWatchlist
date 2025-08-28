package com.learning.StockWatchlist.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="watchlist_stocks")
@IdClass(WatchlistStockId.class)
public class WatchlistStockEntity {

    @Id
    @Column(name="watchlist_id")
    private Long watchlistId;
    
    @Id
    @Column(name="stock_exchange")
    private String stockExchange;
    
    @Id
    @Column(name="stock_ticker")
    private String stockTicker;
    
    @Column(name = "added_at")
    private LocalDateTime addedAt;
    
    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}
