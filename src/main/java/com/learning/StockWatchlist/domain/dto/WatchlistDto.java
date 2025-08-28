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
public class WatchlistDto {
    private Long watchlistId;
    private String name;
    private Long creatorId;
    private String creatorName;
    private Boolean needsRefresh;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
