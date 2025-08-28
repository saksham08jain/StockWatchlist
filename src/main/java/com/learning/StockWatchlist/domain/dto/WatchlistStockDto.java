package com.learning.StockWatchlist.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchlistStockDto {
    private Long watchlistId;
    private String stockExchange;
    private String stockTicker;
    private String stockName;
    private String sector;
    private Boolean isActive;
    private String displayName;
    private LocalDateTime addedAt;
}
