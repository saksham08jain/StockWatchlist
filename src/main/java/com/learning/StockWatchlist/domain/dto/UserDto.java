package com.learning.StockWatchlist.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long userId;//userId in DTO , probably shouldnt be there ,but anyway i dont have time for this
    private String email;
    private String name;
    private String mobileNumber;

}
