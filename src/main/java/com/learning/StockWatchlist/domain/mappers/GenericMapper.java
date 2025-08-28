package com.learning.StockWatchlist.domain.mappers;

public interface GenericMapper<A,B>{

    B mapTo(A a);
    A mapFrom(B b);
}
