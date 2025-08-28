package com.learning.StockWatchlist.controllers;

import com.learning.StockWatchlist.domain.dto.WatchlistStockDto;
import com.learning.StockWatchlist.domain.entity.WatchlistStockEntity;
import com.learning.StockWatchlist.domain.mappers.GenericMapper;
import com.learning.StockWatchlist.services.StockService;
import com.learning.StockWatchlist.services.WatchlistService;
import com.learning.StockWatchlist.services.WatchlistStockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/watchlists/{watchlistId}/stocks")
public class WatchlistStockController {

    private final WatchlistStockService watchlistStockService;
    private final WatchlistService watchlistService;
    private final StockService stockService;
    private final GenericMapper<WatchlistStockEntity, WatchlistStockDto> watchlistStockGenericMapper;

    public WatchlistStockController(
            WatchlistStockService watchlistStockService,
            WatchlistService watchlistService,
            StockService stockService,
            GenericMapper<WatchlistStockEntity, WatchlistStockDto> watchlistStockGenericMapper) {
        this.watchlistStockService = watchlistStockService;
        this.watchlistService = watchlistService;
        this.stockService = stockService;
        this.watchlistStockGenericMapper = watchlistStockGenericMapper;
    }

    @GetMapping
    public ResponseEntity<List<WatchlistStockDto>> getStocksInWatchlist(
            @PathVariable Long watchlistId,
            @RequestParam(required = false) String tickerPattern) {
        
        if (!watchlistService.exists(watchlistId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        List<WatchlistStockEntity> watchlistStocks;
        if (tickerPattern != null && !tickerPattern.isEmpty()) {
            watchlistStocks = watchlistStockService.findByWatchlistIdAndTickerPattern(watchlistId, tickerPattern);
        } else {
            watchlistStocks = watchlistStockService.findByWatchlistId(watchlistId);
        }
        
        List<WatchlistStockDto> dtos = watchlistStocks.stream()
                .map(watchlistStockGenericMapper::mapTo)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping("/{exchange}/{ticker}")
    public ResponseEntity<WatchlistStockDto> addStockToWatchlist(
            @PathVariable Long watchlistId,
            @PathVariable String exchange,
            @PathVariable String ticker) {
        
        if (!watchlistService.exists(watchlistId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        if (!stockService.exists(exchange, ticker)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        if (watchlistStockService.existsInWatchlist(watchlistId, exchange, ticker)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        
        WatchlistStockEntity watchlistStock = new WatchlistStockEntity();
        watchlistStock.setWatchlistId(watchlistId);
        watchlistStock.setStockExchange(exchange);
        watchlistStock.setStockTicker(ticker);
        
        WatchlistStockEntity savedWatchlistStock = watchlistStockService.addStockToWatchlist(watchlistStock);
        return new ResponseEntity<>(watchlistStockGenericMapper.mapTo(savedWatchlistStock), HttpStatus.CREATED);
    }

    @DeleteMapping("/{exchange}/{ticker}")
    public ResponseEntity<Void> removeStockFromWatchlist(
            @PathVariable Long watchlistId,
            @PathVariable String exchange,
            @PathVariable String ticker) {
        
        if (!watchlistService.exists(watchlistId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        if (!watchlistStockService.existsInWatchlist(watchlistId, exchange, ticker)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        watchlistStockService.removeStockFromWatchlist(watchlistId, exchange, ticker);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
