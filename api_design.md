# StockWatchlist API Documentation

## Overview

This document details the API endpoints available in the StockWatchlist application. The API follows RESTful principles and uses JSON for data exchange.

## Base URL

```
http://localhost:8080/api/v1
```

## Authentication

Authentication is not implemented in the current version.

## Response Codes

| Code | Description |
|------|-------------|
| 200  | OK - The request has succeeded |
| 201  | Created - A new resource has been created |
| 204  | No Content - The request has succeeded but returns no message body |
| 400  | Bad Request - The request could not be understood or was missing required parameters |
| 404  | Not Found - Resource was not found |
| 409  | Conflict - Request could not be completed due to a conflict |
| 500  | Internal Server Error - Something went wrong on the server |

## Data Models

### User

```json
{
  "userId": 1,
  "email": "user@example.com",
  "name": "John Doe",
  "mobileNumber": "1234567890",
  "createdAt": "2025-08-19T14:30:00",
  "updatedAt": "2025-08-19T14:30:00"
}
```

### Stock

```json
{
  "exchange": "NYSE",
  "ticker": "AAPL",
  "name": "Apple Inc.",
  "sector": "Technology",
  "isActive": true,
  "delistedAt": null,
  "createdAt": "2025-08-19T14:30:00",
  "updatedAt": "2025-08-19T14:30:00"
}
```

### Watchlist

```json
{
  "watchlistId": 1,
  "name": "Tech Stocks",
  "creatorId": 1,
  "creatorName": "John Doe",
  "needsRefresh": false,
  "createdAt": "2025-08-19T14:30:00",
  "updatedAt": "2025-08-19T14:30:00"
}
```

### WatchlistStock

```json
{
  "watchlistId": 1,
  "stockExchange": "NYSE",
  "stockTicker": "AAPL",
  "stockName": "Apple Inc.",
  "sector": "Technology",
  "isActive": true,
  "displayName": "Apple Inc.",
  "addedAt": "2025-08-19T14:30:00"
}
```

---

## User API Endpoints

### Get All Users

```
GET /users
```

**Response**

```json
[
  {
    "userId": 1,
    "email": "user1@example.com",
    "name": "John Doe",
    "mobileNumber": "1234567890",
    "createdAt": "2025-08-19T14:30:00",
    "updatedAt": "2025-08-19T14:30:00"
  },
  {
    "userId": 2,
    "email": "user2@example.com",
    "name": "Jane Smith",
    "mobileNumber": "0987654321",
    "createdAt": "2025-08-19T14:30:00",
    "updatedAt": "2025-08-19T14:30:00"
  }
]
```

### Get User by ID

```
GET /users/{id}
```

**Parameters**

| Name | Type | Description |
|------|------|-------------|
| id   | Long | User ID     |

**Response**

```json
{
  "userId": 1,
  "email": "user@example.com",
  "name": "John Doe",
  "mobileNumber": "1234567890",
  "createdAt": "2025-08-19T14:30:00",
  "updatedAt": "2025-08-19T14:30:00"
}
```

### Create User

```
POST /users
```

**Request Body**

```json
{
  "email": "newuser@example.com",
  "name": "New User",
  "mobileNumber": "1112223333"
}
```

**Response**

```json
{
  "userId": 3,
  "email": "newuser@example.com",
  "name": "New User",
  "mobileNumber": "1112223333",
  "createdAt": "2025-08-19T14:35:00",
  "updatedAt": "2025-08-19T14:35:00"
}
```

### Update User

```
PUT /users/{id}
```

**Parameters**

| Name | Type | Description |
|------|------|-------------|
| id   | Long | User ID     |

**Request Body**

```json
{
  "email": "updated@example.com",
  "name": "Updated User",
  "mobileNumber": "9998887777"
}
```

**Response**

```json
{
  "userId": 1,
  "email": "updated@example.com",
  "name": "Updated User",
  "mobileNumber": "9998887777",
  "createdAt": "2025-08-19T14:30:00",
  "updatedAt": "2025-08-19T14:40:00"
}
```

### Partial Update User

```
PATCH /users/{id}
```

**Parameters**

| Name | Type | Description |
|------|------|-------------|
| id   | Long | User ID     |

**Request Body**

```json
{
  "name": "Partially Updated Name"
}
```

**Response**

```json
{
  "userId": 1,
  "email": "user@example.com",
  "name": "Partially Updated Name",
  "mobileNumber": "1234567890",
  "createdAt": "2025-08-19T14:30:00",
  "updatedAt": "2025-08-19T14:45:00"
}
```

### Delete User

```
DELETE /users/{id}
```

**Parameters**

| Name | Type | Description |
|------|------|-------------|
| id   | Long | User ID     |

**Response**

```
204 No Content
```

---

## Stock API Endpoints

### Get All Stocks

```
GET /stocks
```

**Query Parameters**

| Name      | Type    | Required | Description                           |
|-----------|---------|----------|---------------------------------------|
| activeOnly| Boolean | No       | Filter to only return active stocks   |

**Response**

```json
[
  {
    "exchange": "NYSE",
    "ticker": "AAPL",
    "name": "Apple Inc.",
    "sector": "Technology",
    "isActive": true,
    "delistedAt": null,
    "createdAt": "2025-08-19T14:30:00",
    "updatedAt": "2025-08-19T14:30:00"
  },
  {
    "exchange": "NASDAQ",
    "ticker": "MSFT",
    "name": "Microsoft Corporation",
    "sector": "Technology",
    "isActive": true,
    "delistedAt": null,
    "createdAt": "2025-08-19T14:30:00",
    "updatedAt": "2025-08-19T14:30:00"
  }
]
```

### Get Stock by Exchange and Ticker

```
GET /stocks/{exchange}/{ticker}
```

**Parameters**

| Name     | Type   | Description            |
|----------|--------|------------------------|
| exchange | String | Stock exchange code    |
| ticker   | String | Stock ticker symbol    |

**Response**

```json
{
  "exchange": "NYSE",
  "ticker": "AAPL",
  "name": "Apple Inc.",
  "sector": "Technology",
  "isActive": true,
  "delistedAt": null,
  "createdAt": "2025-08-19T14:30:00",
  "updatedAt": "2025-08-19T14:30:00"
}
```

### Search Stocks

```
GET /stocks/search
```

**Query Parameters**

| Name     | Type   | Required | Description                      |
|----------|--------|----------|----------------------------------|
| exchange | String | No       | Stock exchange code              |
| ticker   | String | No       | Stock ticker pattern to search   |
| name     | String | No       | Stock name pattern to search     |
| sector   | String | No       | Filter by sector                 |

**Response**

```json
[
  {
    "exchange": "NYSE",
    "ticker": "AAPL",
    "name": "Apple Inc.",
    "sector": "Technology",
    "isActive": true,
    "delistedAt": null,
    "createdAt": "2025-08-19T14:30:00",
    "updatedAt": "2025-08-19T14:30:00"
  }
]
```

### Create Stock

```
POST /stocks
```

**Request Body**

```json
{
  "exchange": "NASDAQ",
  "ticker": "GOOGL",
  "name": "Alphabet Inc.",
  "sector": "Technology",
  "isActive": true
}
```

**Response**

```json
{
  "exchange": "NASDAQ",
  "ticker": "GOOGL",
  "name": "Alphabet Inc.",
  "sector": "Technology",
  "isActive": true,
  "delistedAt": null,
  "createdAt": "2025-08-19T14:50:00",
  "updatedAt": "2025-08-19T14:50:00"
}
```

### Update Stock

```
PUT /stocks/{exchange}/{ticker}
```

**Parameters**

| Name     | Type   | Description            |
|----------|--------|------------------------|
| exchange | String | Stock exchange code    |
| ticker   | String | Stock ticker symbol    |

**Request Body**

```json
{
  "name": "Updated Company Name",
  "sector": "Updated Sector",
  "isActive": true
}
```

**Response**

```json
{
  "exchange": "NYSE",
  "ticker": "AAPL",
  "name": "Updated Company Name",
  "sector": "Updated Sector",
  "isActive": true,
  "delistedAt": null,
  "createdAt": "2025-08-19T14:30:00",
  "updatedAt": "2025-08-19T15:00:00"
}
```

### Partial Update Stock

```
PATCH /stocks/{exchange}/{ticker}
```

**Parameters**

| Name     | Type   | Description            |
|----------|--------|------------------------|
| exchange | String | Stock exchange code    |
| ticker   | String | Stock ticker symbol    |

**Request Body**

```json
{
  "sector": "Technology Services"
}
```

**Response**

```json
{
  "exchange": "NYSE",
  "ticker": "AAPL",
  "name": "Apple Inc.",
  "sector": "Technology Services",
  "isActive": true,
  "delistedAt": null,
  "createdAt": "2025-08-19T14:30:00",
  "updatedAt": "2025-08-19T15:05:00"
}
```

### Soft Delete Stock

```
DELETE /stocks/{exchange}/{ticker}
```

**Parameters**

| Name     | Type   | Description            |
|----------|--------|------------------------|
| exchange | String | Stock exchange code    |
| ticker   | String | Stock ticker symbol    |

**Response**

```
204 No Content
```

Note: This performs a soft delete, marking the stock as inactive instead of removing it from the database.

---

## Watchlist API Endpoints

### Get All Watchlists

```
GET /watchlists
```

**Query Parameters**

| Name   | Type | Required | Description                                |
|--------|------|----------|--------------------------------------------|
| userId | Long | No       | Filter watchlists by creator user ID       |

**Response**

```json
[
  {
    "watchlistId": 1,
    "name": "Tech Stocks",
    "creatorId": 1,
    "creatorName": "John Doe",
    "needsRefresh": false,
    "createdAt": "2025-08-19T14:30:00",
    "updatedAt": "2025-08-19T14:30:00"
  },
  {
    "watchlistId": 2,
    "name": "Financial Stocks",
    "creatorId": 1,
    "creatorName": "John Doe",
    "needsRefresh": false,
    "createdAt": "2025-08-19T14:35:00",
    "updatedAt": "2025-08-19T14:35:00"
  }
]
```

### Get Watchlist by ID

```
GET /watchlists/{id}
```

**Parameters**

| Name | Type | Description      |
|------|------|------------------|
| id   | Long | Watchlist ID     |

**Response**

```json
{
  "watchlistId": 1,
  "name": "Tech Stocks",
  "creatorId": 1,
  "creatorName": "John Doe",
  "needsRefresh": false,
  "createdAt": "2025-08-19T14:30:00",
  "updatedAt": "2025-08-19T14:30:00"
}
```

### Create Watchlist

```
POST /watchlists
```

**Query Parameters**

| Name   | Type | Required | Description      |
|--------|------|----------|------------------|
| userId | Long | Yes      | Creator user ID  |

**Request Body**

```json
{
  "name": "Healthcare Stocks"
}
```

**Response**

```json
{
  "watchlistId": 3,
  "name": "Healthcare Stocks",
  "creatorId": 1,
  "creatorName": "John Doe",
  "needsRefresh": false,
  "createdAt": "2025-08-19T15:10:00",
  "updatedAt": "2025-08-19T15:10:00"
}
```

### Update Watchlist

```
PUT /watchlists/{id}
```

**Parameters**

| Name | Type | Description      |
|------|------|------------------|
| id   | Long | Watchlist ID     |

**Request Body**

```json
{
  "name": "Updated Watchlist Name"
}
```

**Response**

```json
{
  "watchlistId": 1,
  "name": "Updated Watchlist Name",
  "creatorId": 1,
  "creatorName": "John Doe",
  "needsRefresh": false,
  "createdAt": "2025-08-19T14:30:00",
  "updatedAt": "2025-08-19T15:15:00"
}
```

### Partial Update Watchlist

```
PATCH /watchlists/{id}
```

**Parameters**

| Name | Type | Description      |
|------|------|------------------|
| id   | Long | Watchlist ID     |

**Request Body**

```json
{
  "name": "Partially Updated Name"
}
```

**Response**

```json
{
  "watchlistId": 1,
  "name": "Partially Updated Name",
  "creatorId": 1,
  "creatorName": "John Doe",
  "needsRefresh": false,
  "createdAt": "2025-08-19T14:30:00",
  "updatedAt": "2025-08-19T15:20:00"
}
```

### Delete Watchlist

```
DELETE /watchlists/{id}
```

**Parameters**

| Name | Type | Description      |
|------|------|------------------|
| id   | Long | Watchlist ID     |

**Response**

```
204 No Content
```

---

## Watchlist Stock API Endpoints

### Get Stocks in Watchlist

```
GET /watchlists/{watchlistId}/stocks
```

**Parameters**

| Name       | Type   | Description                   |
|------------|--------|-------------------------------|
| watchlistId| Long   | Watchlist ID                  |

**Query Parameters**

| Name         | Type   | Required | Description                             |
|--------------|--------|----------|-----------------------------------------|
| tickerPattern| String | No       | Filter by ticker pattern                |

**Response**

```json
[
  {
    "watchlistId": 1,
    "stockExchange": "NYSE",
    "stockTicker": "AAPL",
    "stockName": "Apple Inc.",
    "sector": "Technology",
    "isActive": true,
    "displayName": "Apple Inc.",
    "addedAt": "2025-08-19T14:40:00"
  },
  {
    "watchlistId": 1,
    "stockExchange": "NASDAQ",
    "stockTicker": "MSFT",
    "stockName": "Microsoft Corporation",
    "sector": "Technology",
    "isActive": true,
    "displayName": "Microsoft Corporation",
    "addedAt": "2025-08-19T14:45:00"
  }
]
```

### Add Stock to Watchlist

```
POST /watchlists/{watchlistId}/stocks/{exchange}/{ticker}
```

**Parameters**

| Name       | Type   | Description                   |
|------------|--------|-------------------------------|
| watchlistId| Long   | Watchlist ID                  |
| exchange   | String | Stock exchange code           |
| ticker     | String | Stock ticker symbol           |

**Response**

```json
{
  "watchlistId": 1,
  "stockExchange": "NASDAQ",
  "stockTicker": "GOOGL",
  "stockName": "Alphabet Inc.",
  "sector": "Technology",
  "isActive": true,
  "displayName": "Alphabet Inc.",
  "addedAt": "2025-08-19T15:25:00"
}
```

### Remove Stock from Watchlist

```
DELETE /watchlists/{watchlistId}/stocks/{exchange}/{ticker}
```

**Parameters**

| Name       | Type   | Description                   |
|------------|--------|-------------------------------|
| watchlistId| Long   | Watchlist ID                  |
| exchange   | String | Stock exchange code           |
| ticker     | String | Stock ticker symbol           |

**Response**

```
204 No Content
```

---

## Key Design Decisions

### Composite Keys for Stocks

Stocks are identified by a composite key of `exchange` and `ticker`, which provides a natural and business-meaningful identifier instead of using an artificial surrogate key.

### Soft Delete for Stocks

Stocks are soft-deleted by setting an `isActive` flag to false and recording the delisting date. This preserves historical data while preventing deleted stocks from appearing in active views.

### Watchlist Refresh Flag

Watchlists have a `needsRefresh` flag that gets set when stocks are added or removed, allowing for efficient materialized view refreshing.

### Separate DTOs from Entities

The application uses separate DTOs (Data Transfer Objects) and entity classes to decouple the API contract from the database model.

### Custom Mappers

Custom mappers handle the conversion between entities and DTOs, including special handling for relationships like Watchlist to User.

## Testing with Postman

1. Import the collection from the API documentation.
2. Set the base URL to `http://localhost:8080/api/v1`.
3. Start with creating a user, then creating stocks, followed by creating a watchlist, and finally adding stocks to the watchlist.
4. Test CRUD operations for each entity type.
5. Verify soft delete functionality by deleting a stock and then querying the watchlist to see it marked as delisted.

## Database Considerations

- The database uses timestamp fields for auditing (`created_at`, `updated_at`).
- Unique constraints prevent duplicate entries.
- Foreign key constraints maintain referential integrity.
- Materialized views optimize read performance for watchlist displays.

---

# End of API Documentation
