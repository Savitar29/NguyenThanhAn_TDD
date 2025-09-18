package com.example.springtest.service;

import com.example.springtest.model.Book;
import com.example.springtest.model.Order;
import com.example.springtest.model.User;
import com.example.springtest.repository.IBookRepository;
import com.example.springtest.repository.IOrderRepository;
import com.example.springtest.repository.IUserRepository;

public class OrderService {
    private IOrderRepository orderRepository;
    private IBookRepository bookRepository;
    private IUserRepository userRepository;

    public OrderService(IOrderRepository oderRepository, IBookRepository bookRepository, IUserRepository userRepository) {
        this.orderRepository = oderRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }
    //tạo đơn
    public Order makeOrder(String email, long bookId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0!");
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Không tìm thấy user!");
        }

        Book book = bookRepository.findById(bookId);

        if (book == null) {
            throw new IllegalArgumentException("Không tìm thay sach!");
        }

        if (quantity > book.getStockQuantity()) {
            throw new IllegalArgumentException("Số lượng phải nhỏ hơn số lượng tồn kho!s");
        }

        // giảm stockQuantity
        book.setStockQuantity(book.getStockQuantity() - quantity);
        bookRepository.save(book);

        // Tạo order
        Order order = Order.builder()
                .userEmail(user.getEmail())
                .bookId(bookId)
                .quantity(quantity)
                .totalPrice(quantity * book.getPrice())
                .build();
        orderRepository.save(order);
        return order;
    }

    //huy đơn
    public void cancelOrder(long orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Đơn hàng không tồn tại!");
        }

        Book book = bookRepository.findById(order.getBookId());
        if (book != null) {
            book.setStockQuantity(book.getStockQuantity() + order.getQuantity());
            bookRepository.save(book);
        }

        orderRepository.delete(order); // Xóa đơn hàng
    }
}
