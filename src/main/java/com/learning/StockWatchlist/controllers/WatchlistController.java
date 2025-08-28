package com.learning.StockWatchlist.controllers;

import com.learning.StockWatchlist.domain.dto.WatchlistDto;
import com.learning.StockWatchlist.domain.entity.UserEntity;
import com.learning.StockWatchlist.domain.entity.WatchlistEntity;
import com.learning.StockWatchlist.domain.mappers.GenericMapper;
import com.learning.StockWatchlist.services.UserService;
import com.learning.StockWatchlist.services.WatchlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/watchlists")
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final UserService userService;
    private final GenericMapper<WatchlistEntity, WatchlistDto> watchlistGenericMapper;

    public WatchlistController(
            WatchlistService watchlistService,
            UserService userService,
            GenericMapper<WatchlistEntity, WatchlistDto> watchlistGenericMapper) {
        this.watchlistService = watchlistService;
        this.userService = userService;
        this.watchlistGenericMapper = watchlistGenericMapper;
    }

    @GetMapping
    public List<WatchlistDto> getAllWatchlists(@RequestParam(required = false) Long userId) {
        List<WatchlistEntity> watchlists;
        if (userId != null) {
            watchlists = watchlistService.findByUserId(userId);
        } else {
            watchlists = watchlistService.findAll();
        }
        return watchlists.stream().map(watchlistGenericMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WatchlistDto> getWatchlist(@PathVariable Long id) {
        Optional<WatchlistEntity> watchlist = watchlistService.findById(id);
        return watchlist.map(w -> new ResponseEntity<>(watchlistGenericMapper.mapTo(w), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<WatchlistDto> createWatchlist(
            @RequestBody WatchlistDto watchlistDto,
            @RequestParam Long userId) {
        
        Optional<UserEntity> user = userService.findOne(userId);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        WatchlistEntity watchlistEntity = watchlistGenericMapper.mapFrom(watchlistDto);
        watchlistEntity.setCreator(user.get());
        
        WatchlistEntity savedWatchlist = watchlistService.save(watchlistEntity);
        return new ResponseEntity<>(watchlistGenericMapper.mapTo(savedWatchlist), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WatchlistDto> updateWatchlist(
            @PathVariable Long id,
            @RequestBody WatchlistDto watchlistDto) {
        
        if (!watchlistService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        WatchlistEntity watchlistEntity = watchlistGenericMapper.mapFrom(watchlistDto);
        WatchlistEntity updatedWatchlist = watchlistService.update(id, watchlistEntity);
        return new ResponseEntity<>(watchlistGenericMapper.mapTo(updatedWatchlist), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WatchlistDto> partialUpdateWatchlist(
            @PathVariable Long id,
            @RequestBody WatchlistDto watchlistDto) {
        
        if (!watchlistService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        WatchlistEntity watchlistEntity = watchlistGenericMapper.mapFrom(watchlistDto);
        WatchlistEntity updatedWatchlist = watchlistService.partialUpdate(id, watchlistEntity);
        return new ResponseEntity<>(watchlistGenericMapper.mapTo(updatedWatchlist), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatchlist(@PathVariable Long id) {
        if (!watchlistService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        watchlistService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
