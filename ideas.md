# StockWatchlist - Ideas & Scratchpad

## Already Implemented

### Core Functionality
- ✅ Basic CRUD operations for Users
- ✅ Basic CRUD operations for Stocks
- ✅ Basic CRUD operations for Watchlists
- ✅ Ability to add/remove stocks from watchlists
- ✅ Composite key implementation for Stocks (exchange + ticker)
- ✅ Soft delete for Stocks
- ✅ Materialized view for optimized watchlist display
- ✅ PostgreSQL database integration

### Architecture
- ✅ Layered architecture (controllers, services, repositories)
- ✅ DTO pattern for data transfer
- ✅ Entity-DTO mapping via custom mappers
- ✅ Basic API design following REST principles

### Testing
- ✅ Verified User flow works (create, update, delete)
- ✅ Manual testing of API endpoints via Postman

## In Progress / Next Steps

### DTO Refinement
- 🔄 Separate Request/Response DTOs
  - UserRequestDto with validation annotations (@NotBlank, @Email, etc.)
  - UserResponseDto with all fields including timestamps
  - Similar patterns for Stock and Watchlist

### Validation & Error Handling
- 🔄 Global exception handling via @ControllerAdvice
  - **Why?** Currently, constraint violations (like duplicate emails) crash with 500 errors
  - **Why?** Error responses are inconsistent across different exception types
  - **Why?** Business logic is mixed with error handling in controllers
  - **Solution:** Centralized handler that translates exceptions to appropriate HTTP status codes

- 🔄 Standard error response format
  - **Why?** Frontend needs consistent error structure for proper display
  - **Why?** Current errors lack details needed for debugging
  - **Structure:** 
    ```json
    {
      "timestamp": "2025-08-25T14:30:00",
      "status": 400,
      "message": "Validation Error",
      "errors": {
        "email": "Email already exists",
        "mobileNumber": "Mobile number must be between 10-15 digits"
      }
    }
    ```

- 🔄 Input validation using Bean Validation
  - **Why?** Currently no validation before hitting database (e.g., blank emails allowed)
  - **Why?** Validation logic is scattered or non-existent
  - **Why?** Database errors are hard to translate into user-friendly messages
  - **Validations needed:**
    - Users: Valid email, name not blank, mobile number format
    - Stocks: Exchange and ticker not blank, name not blank
    - Watchlists: Name not blank

### Database Enhancements
- 🔄 Fix for PUT requests overwriting createdAt with null values
- 🔄 Proper PostgreSQL schema permissions

## Future Ideas

### Architecture Improvements
- 💡 Consider resource-oriented vs. layer-oriented structure
  - Pros: Better cohesion for feature work
  - Cons: Breaks from convention, potentially harder for new developers
- 💡 Implement proper refresh mechanism for materialized views
  - Background job to refresh views with needs_refresh flag

### Feature Ideas
- 💡 Stock price integration with external API
- 💡 User authentication and authorization
- 💡 Email notifications for stock price changes
- 💡 Watchlist sharing between users
- 💡 Performance statistics for watchlists
- 💡 Stock categorization and tagging
- 💡 Mobile-friendly API enhancements

### Technical Debt
- 💡 Comprehensive test suite (unit, integration tests)
- 💡 API documentation with Swagger/OpenAPI
- 💡 Improved logging strategy
- 💡 Performance optimizations for large watchlists

### Advanced Features
- 💡 Real-time stock updates with WebSockets
- 💡 Portfolio tracking (beyond watchlists)
- 💡 Historical price charts
- 💡 Stock comparison tools
- 💡 Alerts and notifications system
- 💡 Machine learning for stock recommendations

## Implementation Notes

### Request/Response DTO Separation
```java
// Instead of one DTO:
public class UserDto {
    private Long id;
    private String email;
    private String name;
    // ...
}

// Split into request:
public class UserRequestDto {
    @NotBlank @Email
    private String email;
    
    @NotBlank
    private String name;
    // ...
}

// And response:
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // ...
}
```

### Global Exception Handler Implementation
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            errors
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        
        // Extract constraint name from the exception message
        String message = ex.getMostSpecificCause().getMessage();
        if (message.contains("unique constraint") || message.contains("Duplicate entry")) {
            if (message.contains("email")) {
                errors.put("email", "Email already exists");
            } else if (message.contains("mobile_number")) {
                errors.put("mobileNumber", "Mobile number already exists");
            } else {
                errors.put("general", "A database constraint was violated");
            }
        } else {
            errors.put("general", "Database error occurred");
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.CONFLICT.value(),
            "Database Constraint Violation",
            errors
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("resource", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Resource Not Found",
            errors
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
```

### Validation in Controllers
```java
@PostMapping
public ResponseEntity<StockResponseDto> createStock(@Valid @RequestBody StockRequestDto stockDto) {
    StockEntity stockEntity = stockGenericMapper.mapFrom(stockDto);
    StockEntity savedStock = stockService.save(stockEntity);
    return new ResponseEntity<>(stockGenericMapper.mapTo(savedStock), HttpStatus.CREATED);
}
```

### Fix for createdAt/updatedAt Handling
```java
@Override
public UserEntity save(UserEntity userEntity) {
    if (userEntity.getUserId() != null) {
        // Update operation - preserve createdAt
        UserEntity existingUser = userRepository.findById(userEntity.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        userEntity.setCreatedAt(existingUser.getCreatedAt());
    } else {
        // New entity - set createdAt
        userEntity.setCreatedAt(LocalDateTime.now());
    }
    
    // Always update updatedAt
    userEntity.setUpdatedAt(LocalDateTime.now());
    
    return userRepository.save(userEntity);
}
```

### Watchlist Refresh Implementation
```java
@Scheduled(fixedRate = 60000) // Run every minute
public void refreshStaleWatchlists() {
    List<WatchlistEntity> staleWatchlists = watchlistRepository.findByNeedsRefreshTrue();
    
    if (!staleWatchlists.isEmpty()) {
        // Execute the REFRESH MATERIALIZED VIEW
        entityManager.createNativeQuery("REFRESH MATERIALIZED VIEW watchlist_display").executeUpdate();
        
        // Reset the flags
        staleWatchlists.forEach(watchlist -> {
            watchlist.setNeedsRefresh(false);
            watchlistRepository.save(watchlist);
        });
    }
}
```

## Questions to Explore

1. How should we handle user authentication and authorization?
2. What's the best way to implement versioning for our API?
3. Should we implement caching, and at what level?
4. How can we optimize for mobile clients?
5. What monitoring and observability tools should we integrate?
6. Should we implement a rate limiting strategy?

---

*This document is a living scratchpad. Add ideas, implementation notes, and thoughts as they come up during development.*
