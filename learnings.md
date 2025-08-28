# learnings.md

# Software Engineering Learnings: First Principles & Insights

## Core Architecture Principles

### Separation of Concerns
- **First Principle**: Each component should have a single, well-defined responsibility.
- **Application**: The separation of controllers, services, repositories, and DTOs in our application enforces clear boundaries of responsibility.
- **Insight**: When responsibilities blur (e.g., business logic in controllers), the system becomes harder to test, maintain, and reason about.

### Data Transfer Object (DTO) Pattern
- **First Principle**: Domain objects shouldn't be directly exposed across layer boundaries.
- **Application**: We use DTOs to transfer data between our API layer and service layer.
- **Aha Moment**: The realization that request DTOs and response DTOs serve fundamentally different purposes - validation vs. presentation.
- **Theoretical Basis**: Information hiding (Parnas) and interface segregation principle (SOLID).

### Repository Pattern
- **First Principle**: Data access logic should be abstracted behind a collection-like interface.
- **Application**: Our repositories provide methods for CRUD operations without exposing database implementation details.
- **Insight**: This creates a clear boundary between domain logic and data access, enabling easier testing and flexibility in data source.

## Database Design Principles

### Normalization vs. Performance
- **First Principle**: Data should be stored without redundancy (normalization), but query performance may require denormalization.
- **Application**: Our schema uses normalized core tables with materialized views for performance.
- **Aha Moment**: "Normalize for correctness, materialize for performance" - balancing theoretical purity with practical needs.

### Natural vs. Surrogate Keys
- **First Principle**: Keys should uniquely identify records while supporting the domain model's integrity.
- **Application**: Using composite keys (exchange, ticker) for stocks instead of artificial IDs.
- **Theoretical Basis**: Relational theory (Codd) advocates that keys should have semantic meaning when possible.
- **Insight**: Domain-meaningful keys enhance readability and reduce joins in certain scenarios.

### Soft Delete Pattern
- **First Principle**: Data is valuable and should rarely be permanently destroyed.
- **Application**: Using `is_active` flags rather than physical deletion.
- **Insight**: This preserves historical data and relationships but introduces complexity in queries and views.






## System Design Patterns

### Materialized View Pattern
- **First Principle**: Precomputing complex queries can dramatically improve read performance.
- **Application**: Our schema uses materialized views for frequently accessed data.
- **Insight**: This introduces eventual consistency but accepts it as a trade-off for performance.

### Composite Key Pattern
- **First Principle**: Some entities are naturally identified by multiple attributes.
- **Application**: Our stock entities use exchange+ticker as a natural composite key.
- **Insight**: This better represents the domain at the cost of slightly more complex queries.

## StockWatchlist-Specific Insights




### Request/Response DTO Separation in Practice
- **First Principle**: Input validation and output formatting serve different purposes.
- **Direct Experience**: Observing the PUT request overwriting `createdAt` with null values.
- **Aha Moment**: Realizing we needed separate DTOs for request (with validation) and response (with all fields).
- **Code Impact**: This pattern dramatically simplified our controllers and validation logic.

### Soft Delete Implementation Challenges
- **First Principle**: Logical deletion preserves data but complicates queries.
- **Direct Experience**: Implementing the soft delete pattern for stocks using `is_active` and `delisted_at`.
- **Specific Challenge**: Ensuring that watchlists display stocks marked as delisted with the appropriate `[DELISTED]` prefix.
- **Solution**: Using CASE statements in our materialized view to dynamically adjust the display name based on deletion status.

### Natural Keys for Domain Objects
- **First Principle**: Identifiers should reflect the domain where appropriate.
- **Direct Experience**: Using the exchange+ticker composite key for stocks instead of surrogate keys.
- **Specific Benefit**: This made our API more intuitive (`/stocks/NYSE/AAPL` instead of `/stocks/123`).
- **Trade-off**: Slightly more complex JOIN operations in our repository queries, but worth it for the clearer domain model.

### Staleness Tracking Pattern
- **First Principle**: Denormalized data becomes stale when the source changes.
- **Direct Experience**: Implementing the `needs_refresh` flag on watchlists to track when the materialized view needs updating.
- **Concrete Example**: When adding/removing stocks from a watchlist, we mark it for refresh rather than immediately refreshing.
- **Aha Moment**: Realizing this pattern could enable a background job to efficiently refresh only stale data.


### Global Exception Handler Implementation
- **First Principle**: Centralizing error handling improves consistency.
- **Direct Experience**: Creating a `GlobalExceptionHandler` with `@ControllerAdvice` to catch different exception types.
- **Specific Pattern**: Different handling for validation errors, not-found conditions, and database constraint violations.
- **API Improvement**: This allowed us to return well-structured error responses with appropriate HTTP status codes.


---

*This document represents our ongoing learning journey with the StockWatchlist project. As we continue to develop and refine the application, we'll add new insights and "aha moments" to this living document.*