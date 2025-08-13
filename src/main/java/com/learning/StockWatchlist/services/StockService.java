package com.learning.StockWatchlist.services;

import com.learning.StockWatchlist.domain.dto.StockDto;
import com.learning.StockWatchlist.domain.entity.StockEntity;

import java.util.List;
import java.util.Optional;

public interface StockService
{
    //What do i want StockService to do?
    //we are keeping the contract and implementation separate
    //probably because its easy to switch in and out
    /*
    i want a
    findALl
    findOne
    saveOne
    deleteOne
     */
    List<StockEntity> findAll();
    Optional<StockEntity>findOne(String stockId);
    StockEntity save(StockEntity u);
    void delete(String StockId);

    StockEntity partialUpdate(String StockId, StockDto StockDto);


    boolean isExists(String StockId);



}
