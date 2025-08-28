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
@Table(name="watchlists")
public class WatchlistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="watchlist_id")
    private Long watchlistId;
    
    @Column
    @Builder.Default
    private String name = "My Watchlist";
    
    @ManyToOne
    @JoinColumn(name = "creator", referencedColumnName = "user_id")
    private UserEntity creator;
    
    @Column(name = "needs_refresh")
    @Builder.Default
    private Boolean needsRefresh = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
