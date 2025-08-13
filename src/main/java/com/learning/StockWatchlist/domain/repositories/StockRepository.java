package com.learning.StockWatchlist.domain.repositories;

import com.learning.StockWatchlist.domain.entity.StockEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends CrudRepository<StockEntity,String> {
}
