package com.example.springtest.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private long id;
    private String userEmail;
    private long bookId;
    private int quantity;
    private double totalPrice;
}
