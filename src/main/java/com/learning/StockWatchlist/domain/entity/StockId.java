package com.learning.StockWatchlist.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockId implements Serializable {
    private String exchange;
    private String ticker;
}
