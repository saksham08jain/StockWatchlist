package com.learning.StockWatchlist.domain.repositories;

import com.learning.StockWatchlist.domain.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity,Long>{

}
