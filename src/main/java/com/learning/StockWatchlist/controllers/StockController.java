package com.learning.StockWatchlist.controllers;

import com.learning.StockWatchlist.domain.dto.StockDto;
import com.learning.StockWatchlist.domain.entity.StockEntity;
import com.learning.StockWatchlist.domain.mappers.Mapper;
import com.learning.StockWatchlist.services.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
//why am i not going through stockcontroller then stockcontrollerimpl? cause i dont need swapping ability?
//hmmm
@RequestMapping("/api/v1")
public class StockController {
    private StockService stockService;
    private Mapper<StockEntity, StockDto> stockMapper;

    public StockController(StockService stockService, Mapper<StockEntity, StockDto> stockMapper)
    {
        this.stockMapper=stockMapper;
        this.stockService=stockService;
    }

    @GetMapping(path= "/stocks")
    public List<StockDto> getAllStocks()
    {
        
        List<StockEntity> stockEntities=stockService.findAll();
        

       return stockEntities.stream().map(stockMapper::mapTo).collect(Collectors.toList());

    }
    @GetMapping(path="/stocks/{ticker}")

    public ResponseEntity<StockDto> getStock(@PathVariable("ticker") String stockTicker)
    {
        Optional<StockEntity> foundStock=stockService.findOne(stockTicker);

        return  foundStock.map(stockEntity -> {
                    StockDto stockDto=stockMapper.mapTo(stockEntity);
                    return new ResponseEntity<>(stockDto,HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        
    }

   
    @PostMapping(path = "/stocks")
    public ResponseEntity<StockDto> createStock(@RequestBody StockDto stock) {
        StockEntity stockEntity = stockMapper.mapFrom(stock);
        StockEntity savedStockEntity = stockService.save(stockEntity);
        return new ResponseEntity<>(stockMapper.mapTo(savedStockEntity), HttpStatus.CREATED);
    }

    @PutMapping(path = "/stocks/{ticker}")
    public ResponseEntity<StockDto> fullUpdateStock(
            @PathVariable("ticker") String stockTicker,
            @RequestBody StockDto stockDto) {

        if(!stockService.isExists(stockTicker)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        stockDto.setTicker(stockTicker);
        StockEntity stockEntity = stockMapper.mapFrom(stockDto);
        StockEntity savedStockEntity = stockService.save(stockEntity);
        return new ResponseEntity<>(
                stockMapper.mapTo(savedStockEntity),
                HttpStatus.OK);
    }



    @PatchMapping("/stocks/{ticker}")
    public ResponseEntity<StockDto> partialUpdateStock(
            @PathVariable("ticker") String stockTicker,
            @RequestBody StockDto incomingStock) {

        if (!stockService.isExists(stockTicker)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        StockEntity updatedStock = stockService.partialUpdate(stockTicker, incomingStock);
        return new ResponseEntity<>(
                stockMapper.mapTo(updatedStock),
                HttpStatus.OK);
    }
    @DeleteMapping(path = "/stocks/{ticker}")
    public ResponseEntity deleteStock(@PathVariable("ticker") String stockTicker) {
        stockService.delete(stockTicker);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
