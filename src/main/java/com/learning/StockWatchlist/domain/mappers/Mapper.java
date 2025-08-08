package com.learning.StockWatchlist.domain.mappers;

public interface Mapper <A,B>{

    B mapTo(A a);
    A mapFrom(B b);
}
