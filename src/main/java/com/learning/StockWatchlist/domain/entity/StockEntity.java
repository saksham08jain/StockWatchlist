package com.learning.StockWatchlist.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="stocks")
public class StockEntity {




        @Id
        @Column(name="ticker")
        private String ticker;

        private String name;

        private String sector;
    }

