package com.learning.StockWatchlist.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long userId;
    @Column(nullable = false,unique = true)
    private  String email;
    @Column(nullable = false)
    private String name;
    @Column(name = "mobile_number",unique = true,nullable = false)
    private String mobileNumber;
}
