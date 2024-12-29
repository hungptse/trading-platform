package com.aquariux.trading_platform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "user_wallet")
@AllArgsConstructor
@NoArgsConstructor
public class UserWallet {
    @Id
    private Long userId;
    private String username;
    private String balanceJSON;
}

