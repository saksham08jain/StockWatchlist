# StockWatchlist Application Flow

## Overview

This document explains the flow of data and operations within the StockWatchlist application, from API requests to database operations.

## Architecture Overview

The StockWatchlist application follows a layered architecture:

```
┌─────────────┐
│  Controller │  API Layer - Handles HTTP requests/responses
└──────┬──────┘
       │
┌──────▼──────┐
│   Service   │  Business Logic Layer - Implements business rules
└──────┬──────┘
       │
┌──────▼──────┐
│ Repository  │  Data Access Layer - Interacts with the database
└──────┬──────┘
       │
┌──────▼──────┐
│  Database   │  Persistent Storage
└─────────────┘
```

## Core Flows

### User Management Flow

1. **Create User**
   ```
   Client → UserController.createUser() → UserService.save() → UserRepository.save() → Database
   ```

2. **Retrieve User**
   ```
   Client → UserController.getUser() → UserService.findOne() → UserRepository.findById() → Database → UserEntity → UserMapper.mapTo() → UserDto → Client
   ```

3. **Update User**
   ```
   Client → UserController.updateUser() → UserService.save() → UserRepository.save() → Database
   ```

4. **Partial Update User**
   ```
   Client → UserController.partialUpdateUser() → UserService.partialUpdate() → UserRepository.findById() → Update fields → UserRepository.save() → Database
   ```

5. **Delete User**
   ```
   Client → UserController.deleteUser() → UserService.delete() → UserRepository.deleteById() → Database
   ```

### Stock Management Flow

1. **Create Stock**
   ```
   Client → StockController.createStock() → StockService.save() → StockRepository.save() → Database
   ```

2. **Retrieve Stock**
   ```
   Client → StockController.getStock() → StockService.findById() → StockRepository.findById() → Database → StockEntity → StockMapper.mapTo() → StockDto → Client
   ```

3. **Search Stocks**
   ```
   Client → StockController.searchStocks() → StockService.findByExchangeAndTickerPattern()/findByNamePattern()/findBySector() → StockRepository.find*() → Database → List<StockEntity> → StockMapper.mapTo() → List<StockDto> → Client
   ```

4. **Update Stock**
   ```
   Client → StockController.updateStock() → StockService.update() → StockRepository.save() → Database
   ```

5. **Partial Update Stock**
   ```
   Client → StockController.partialUpdateStock() → StockService.partialUpdate() → StockRepository.findById() → Update fields → StockRepository.save() → Database
   ```

6. **Soft Delete Stock**
   ```
   Client → StockController.softDeleteStock() → StockService.softDelete() → StockRepository.findById() → Set isActive=false, delistedAt=now → StockRepository.save() → Database
   ```

### Watchlist Management Flow

1. **Create Watchlist**
   ```
   Client → WatchlistController.createWatchlist() → UserService.findOne() → WatchlistService.save() → WatchlistRepository.save() → Database
   ```

2. **Retrieve Watchlist**
   ```
   Client → WatchlistController.getWatchlist() → WatchlistService.findById() → WatchlistRepository.findById() → Database → WatchlistEntity → WatchlistMapper.mapTo() → WatchlistDto → Client
   ```

3. **Find User's Watchlists**
   ```
   Client → WatchlistController.getAllWatchlists(userId) → WatchlistService.findByUserId() → WatchlistRepository.findByCreator_UserId() → Database → List<WatchlistEntity> → WatchlistMapper.mapTo() → List<WatchlistDto> → Client
   ```

4. **Update Watchlist**
   ```
   Client → WatchlistController.updateWatchlist() → WatchlistService.update() → WatchlistRepository.save() → Database
   ```

5. **Delete Watchlist**
   ```
   Client → WatchlistController.deleteWatchlist() → WatchlistService.delete() → WatchlistRepository.deleteById() → Database
   ```

### Watchlist Stock Management Flow

1. **Add Stock to Watchlist**
   ```
   Client → WatchlistStockController.addStockToWatchlist() → WatchlistService.exists() → StockService.exists() → WatchlistStockService.existsInWatchlist() → WatchlistStockService.addStockToWatchlist() → WatchlistService.markForRefresh() → WatchlistStockRepository.save() → Database
   ```

2. **Get Stocks in Watchlist**
   ```
   Client → WatchlistStockController.getStocksInWatchlist() → WatchlistService.exists() → WatchlistStockService.findByWatchlistId()/findByWatchlistIdAndTickerPattern() → WatchlistStockRepository.find*() → Database → List<WatchlistStockEntity> → WatchlistStockMapper.mapTo() → List<WatchlistStockDto> → Client
   ```

3. **Remove Stock from Watchlist**
   ```
   Client → WatchlistStockController.removeStockFromWatchlist() → WatchlistService.exists() → WatchlistStockService.existsInWatchlist() → WatchlistStockService.removeStockFromWatchlist() → WatchlistStockRepository.deleteBy*() → WatchlistService.markForRefresh() → Database
   ```

## Data Flow Diagrams

### User Creation Flow

```
┌──────────┐      ┌───────────────┐      ┌──────────────┐      ┌──────────────┐
│  Client  │─────▶│UserController │─────▶│ UserService  │─────▶│UserRepository│
└──────────┘      └───────────────┘      └──────────────┘      └──────────────┘
                         │                       │                     │
                         │                       │                     │
                  ┌──────▼──────┐        ┌──────▼──────┐       ┌──────▼──────┐
                  │  UserDto    │        │ UserEntity  │       │  Database   │
                  └─────────────┘        └─────────────┘       └─────────────┘
                         ▲                       │                     │
                         │                       │                     │
                         └───────────────────────┴─────────────────────┘
```

### Stock Search Flow

```
┌──────────┐      ┌────────────────┐      ┌───────────────┐      ┌────────────────┐
│  Client  │─────▶│StockController │─────▶│ StockService  │─────▶│StockRepository │
└──────────┘      └────────────────┘      └───────────────┘      └────────────────┘
      ▲                    │                      │                       │
      │                    │                      │                       │
      │            ┌───────▼──────┐      ┌───────▼──────┐         ┌──────▼──────┐
      │            │ Search Params │      │ Query Logic  │         │  Database   │
      │            └───────────────┘      └──────────────┘         └─────────────┘
      │                                           │                       │
      │                                    ┌──────▼──────┐                │
      │                                    │List<StockEntity>│◀────────────┘
      │                                    └──────────────┘
      │                                           │
┌─────┴──────┐                              ┌─────▼──────┐
│List<StockDto>│◀─────────────────────────────│StockMapper │
└─────────────┘                              └────────────┘
```

### Add Stock to Watchlist Flow

```
┌──────────┐     ┌───────────────────┐     ┌─────────────────────┐
│  Client  │────▶│WatchlistStockCtrl │────▶│WatchlistStockService│
└──────────┘     └───────────────────┘     └─────────────────────┘
                          │                           │
                  ┌───────▼───────┐          ┌───────▼────────┐
                  │Check if exists│          │Create Watchlist│
                  └───────────────┘          │Stock Entity    │
                          │                  └────────────────┘
                          │                           │
                  ┌───────▼───────┐          ┌───────▼────────┐
                  │WatchlistService│         │WatchlistStock  │
                  │StockService    │         │Repository      │
                  └───────────────┘          └────────────────┘
                                                      │
                                              ┌───────▼────────┐
                                              │    Database    │
                                              └────────────────┘
                                                      │
                                              ┌───────▼────────┐
                                              │Mark Watchlist  │
                                              │for refresh     │
                                              └────────────────┘
```

## Entity Relationships

```
┌───────────┐       ┌─────────────┐       ┌────────────────┐
│UserEntity │       │WatchlistEntity│     │WatchlistStockEntity│
├───────────┤       ├─────────────┤       ├────────────────┤
│userId     │◀──────│creator     │       │watchlistId     │
│email      │       │watchlistId  │◀──────│stockExchange   │
│name       │       │name         │       │stockTicker     │
│mobileNumber│      │needsRefresh │       │addedAt         │
│createdAt   │      │createdAt    │       └────────────────┘
│updatedAt   │      │updatedAt    │              ▲
└───────────┘       └─────────────┘              │
                                                 │
                                         ┌───────┴──────┐
                                         │ StockEntity  │
                                         ├──────────────┤
                                         │exchange      │
                                         │ticker        │
                                         │name          │
                                         │sector        │
                                         │isActive      │
                                         │delistedAt    │
                                         │createdAt     │
                                         │updatedAt     │
                                         └──────────────┘
```

## Mapper Flow

```
┌────────────┐                          ┌────────────┐
│Entity Layer│                          │  DTO Layer │
├────────────┤                          ├────────────┤
│UserEntity  │───────────────────────────▶│UserDto    │
│            │◀───────────────────────────│           │
└────────────┘         Mapper            └────────────┘
      │                                         │
      │                                         │
      ▼                                         ▼
┌────────────┐                          ┌────────────┐
│StockEntity │───────────────────────────▶│StockDto   │
│            │◀───────────────────────────│           │
└────────────┘         Mapper            └────────────┘
      │                                         │
      │                                         │
      ▼                                         ▼
┌────────────┐                          ┌────────────┐
│WatchlistEntity───────────────────────────▶│WatchlistDto│
│            │◀───────────────────────────│           │
└────────────┘     Custom Mapper         └────────────┘
      │                                         │
      │                                         │
      ▼                                         ▼
┌────────────┐                          ┌────────────┐
│WatchlistStockEntity─────────────────────▶│WatchlistStockDto│
│            │◀───────────────────────────│           │
└────────────┘         Mapper            └────────────┘
```

## Exception Flow

```
┌──────────┐     ┌────────────┐     ┌───────────┐     ┌────────────┐
│  Client  │────▶│ Controller │────▶│  Service  │────▶│ Repository │
└──────────┘     └────────────┘     └───────────┘     └────────────┘
      ▲                │                  │                 │
      │                │                  │                 │
      │         ┌──────▼──────┐    ┌─────▼────┐     ┌──────▼──────┐
      │         │HTTP Response│    │Exception │     │   Database  │
      │         │Status Code  │◀───│  Thrown  │◀────│Exception or │
      │         └─────────────┘    └──────────┘     │Not Found    │
      │                │                             └─────────────┘
      └────────────────┘
```

## Transaction Flow

```
┌───────────────────┐
│Client Request     │
└─────────┬─────────┘
          │
┌─────────▼─────────┐
│Controller         │
└─────────┬─────────┘
          │
┌─────────▼─────────┐
│@Transactional     │
│Service Method     │
└─────────┬─────────┘
          │
┌─────────▼─────────┐    ┌─────────────────┐
│Repository Method 1│───▶│Database         │
└─────────┬─────────┘    │Operation 1      │
          │              └─────────────────┘
┌─────────▼─────────┐    ┌─────────────────┐
│Repository Method 2│───▶│Database         │
└─────────┬─────────┘    │Operation 2      │
          │              └─────────────────┘
┌─────────▼─────────┐
│If Success:        │
│Commit Transaction │
└─────────┬─────────┘
          │
┌─────────▼─────────┐
│If Failure:        │
│Rollback Transaction│
└───────────────────┘
```

## Materialized View Refresh Flow

```
┌────────────────────┐
│Add/Remove Stock    │
│from Watchlist      │
└──────────┬─────────┘
           │
┌──────────▼─────────┐
│WatchlistService    │
│markForRefresh()    │
└──────────┬─────────┘
           │
┌──────────▼─────────┐
│Update Watchlist    │
│needsRefresh = true │
└──────────┬─────────┘
           │
┌──────────▼─────────┐
│Background Job      │
│(not implemented)   │
└──────────┬─────────┘
           │
┌──────────▼─────────┐
│Refresh Materialized│
│View for Watchlists │
│with needsRefresh=true
└──────────┬─────────┘
           │
┌──────────▼─────────┐
│Reset needsRefresh  │
│flag to false       │
└────────────────────┘
```

## Composite Key Handling Flow

```
┌──────────────────┐
│Client Request    │
│with exchange     │
│and ticker        │
└────────┬─────────┘
         │
┌────────▼─────────┐
│Controller        │
│Extracts exchange │
│and ticker        │
└────────┬─────────┘
         │
┌────────▼─────────┐
│Service Method    │
│Creates StockId   │
│Object            │
└────────┬─────────┘
         │
┌────────▼─────────┐
│Repository        │
│uses StockId for  │
│lookup            │
└────────┬─────────┘
         │
┌────────▼─────────┐
│JPA/Hibernate     │
│translates to SQL │
│WHERE clause      │
└────────┬─────────┘
         │
┌────────▼─────────┐
│Database returns  │
│matching record   │
└────────┬─────────┘
         │
┌────────▼─────────┐
│StockEntity       │
│with populated    │
│fields            │
└────────┬─────────┘
         │
┌────────▼─────────┐
│Response returned │
│to client         │
└──────────────────┘
```

## Lifecycle Hooks Flow

```
┌────────────────┐
│Create/Update   │
│Entity Request  │
└───────┬────────┘
        │
┌───────▼────────┐
│JPA Entity      │
│Instantiated    │
└───────┬────────┘
        │
┌───────▼────────┐
│@PrePersist or  │
│@PreUpdate Hook │
│Invoked         │
└───────┬────────┘
        │
┌───────▼────────┐
│Set createdAt/  │
│updatedAt Fields│
└───────┬────────┘
        │
┌───────▼────────┐
│Entity Saved    │
│to Database     │
└───────┬────────┘
        │
┌───────▼────────┐
│Response with   │
│Timestamp Fields│
└────────────────┘
```

## API Error Handling Flow

```
┌────────────────┐
│Client Request  │
└───────┬────────┘
        │
┌───────▼────────┐
│Controller      │
│Validation      │
└───────┬────────┘
        │
        │ If Valid
        ▼
┌────────────────┐    ┌─────────────────┐
│Service Logic   │───▶│ 200/201 Success │
└───────┬────────┘    └─────────────────┘
        │
        │ If Error
        ▼
┌────────────────┐
│Exception Thrown│
└───────┬────────┘
        │
        │ Handled By
        ▼
┌────────────────────────────────┐
│HTTP Status Code and Error Body │
├────────────────────────────────┤
│404 - Resource Not Found        │
│400 - Bad Request               │
│409 - Conflict                  │
│500 - Server Error              │
└────────────────────────────────┘
```

## Watchlist Stock Management Flow

```
┌───────────────────┐
│View Watchlist     │
└────────┬──────────┘
         │
┌────────▼──────────┐
│Find Stocks        │
│in Watchlist       │
└────────┬──────────┘
         │
┌────────▼──────────┐
│Display Stock      │
│Details Including  │
│[DELISTED] Status  │
└────────┬──────────┘
         │
┌────────▼──────────┐           ┌─────────────────┐
│Add Stock to       │◀──────────│Search Available │
│Watchlist          │           │Stocks           │
└────────┬──────────┘           └─────────────────┘
         │
┌────────▼──────────┐
│Mark Watchlist as  │
│Needs Refresh      │
└────────┬──────────┘
         │
┌────────▼──────────┐
│Remove Stock from  │
│Watchlist          │
└────────┬──────────┘
         │
┌────────▼──────────┐
│Mark Watchlist as  │
│Needs Refresh      │
└───────────────────┘
```

## Summary

This document outlines the flow of operations in the StockWatchlist application, showing how data moves through the various layers of the architecture. Understanding these flows will help with debugging, extending the application, and ensuring that new features follow the established patterns.

For more detailed information about the API endpoints and data models, please refer to the API Documentation.
