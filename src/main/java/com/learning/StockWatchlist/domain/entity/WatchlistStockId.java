package com.learning.StockWatchlist.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistStockId implements Serializable {
    private Long watchlistId;
    private String stockExchange;
    private String stockTicker;
}
