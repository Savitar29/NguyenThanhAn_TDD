package com.example.springtest.repository;

import com.example.springtest.model.Order;

public interface IOrderRepository {
    Order save(Order any);
    Order findById(long id);

    void delete(Order order);
}
