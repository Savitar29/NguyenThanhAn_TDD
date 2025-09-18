package com.example.springtest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private long id;
    private String title;
    private String author;
    private double price;
    private int stockQuantity;
}
