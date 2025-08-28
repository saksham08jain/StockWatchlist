package com.learning.StockWatchlist.controllers;

import com.learning.StockWatchlist.domain.dto.StockDto;
import com.learning.StockWatchlist.domain.entity.StockEntity;
import com.learning.StockWatchlist.domain.mappers.GenericMapper;
import com.learning.StockWatchlist.services.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/stocks")
public class StockController {
    private final StockService stockService;
    private final GenericMapper<StockEntity, StockDto> stockGenericMapper;

    public StockController(StockService stockService, GenericMapper<StockEntity, StockDto> stockGenericMapper) {
        this.stockGenericMapper = stockGenericMapper;
        this.stockService = stockService;
    }

    @GetMapping
    public List<StockDto> getAllStocks(@RequestParam(required = false) Boolean activeOnly) {
        List<StockEntity> stocks;
        if (activeOnly != null && activeOnly) {
            stocks = stockService.findAllActive();
        } else {
            stocks = stockService.findAll();
        }
        return stocks.stream().map(stockGenericMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping("/{exchange}/{ticker}")
    public ResponseEntity<StockDto> getStock(
            @PathVariable String exchange, 
            @PathVariable String ticker) {
        Optional<StockEntity> stock = stockService.findById(exchange, ticker);
        return stock.map(s -> new ResponseEntity<>(stockGenericMapper.mapTo(s), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public List<StockDto> searchStocks(
            @RequestParam(required = false) String exchange,
            @RequestParam(required = false) String ticker,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sector) {

        List<StockEntity> stocks;
        if (exchange != null && ticker != null) {
            stocks = stockService.findByExchangeAndTickerPattern(exchange, ticker);
        } else if (name != null) {
            stocks = stockService.findByNamePattern(name);
        } else if (sector != null) {
            stocks = stockService.findBySector(sector);
        } else {
            stocks = stockService.findAllActive();
        }
        return stocks.stream().map(stockGenericMapper::mapTo).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<StockDto> createStock(@RequestBody StockDto stockDto) {
        StockEntity stockEntity = stockGenericMapper.mapFrom(stockDto);
        StockEntity savedStock = stockService.save(stockEntity);
        return new ResponseEntity<>(stockGenericMapper.mapTo(savedStock), HttpStatus.CREATED);
    }

    @PutMapping("/{exchange}/{ticker}")
    public ResponseEntity<StockDto> updateStock(
            @PathVariable String exchange,
            @PathVariable String ticker,
            @RequestBody StockDto stockDto) {
        if (!stockService.exists(exchange, ticker)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        StockEntity stockEntity = stockGenericMapper.mapFrom(stockDto);
        StockEntity updatedStock = stockService.update(exchange, ticker, stockEntity);
        return new ResponseEntity<>(stockGenericMapper.mapTo(updatedStock), HttpStatus.OK);
    }

    @PatchMapping("/{exchange}/{ticker}")
    public ResponseEntity<StockDto> partialUpdateStock(
            @PathVariable String exchange,
            @PathVariable String ticker,
            @RequestBody StockDto stockDto) {
        if (!stockService.exists(exchange, ticker)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        StockEntity stockEntity = stockGenericMapper.mapFrom(stockDto);
        StockEntity updatedStock = stockService.partialUpdate(exchange, ticker, stockEntity);
        return new ResponseEntity<>(stockGenericMapper.mapTo(updatedStock), HttpStatus.OK);
    }

    @DeleteMapping("/{exchange}/{ticker}")
    public ResponseEntity<Void> softDeleteStock(
            @PathVariable String exchange,
            @PathVariable String ticker) {
        if (!stockService.exists(exchange, ticker)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        stockService.softDelete(exchange, ticker);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
