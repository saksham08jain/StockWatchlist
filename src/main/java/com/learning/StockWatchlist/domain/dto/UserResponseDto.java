package com.learning.StockWatchlist.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long userId;
    private String email;
    private String name;
    private String mobileNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


