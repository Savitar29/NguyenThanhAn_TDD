package com.example.springtest.service;

import com.example.springtest.model.Book;
import com.example.springtest.model.Order;
import com.example.springtest.model.User;
import com.example.springtest.repository.IBookRepository;
import com.example.springtest.repository.IOrderRepository;
import com.example.springtest.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private IBookRepository bookRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void throw_error_if_quantity_is_not_greater_or_equal_to_1() {
        int quantity = -1;
        int bookId = 1;
        String userEmail = "savitar@gmail.com";

        assertThrows(IllegalArgumentException.class, () -> {orderService.makeOrder(userEmail, bookId, quantity);});
    }


    @Test
    public void throw_error_if_quantity_is_greater_than_stock_quantity() {
        int quantity = 10;
        int bookId = 1;
        int stockQuantity = 1;
        String userEmail = "savitar@gmail.com";

        Book book = Book.builder().id(bookId).stockQuantity(stockQuantity).build();
        User user = User.builder().email(userEmail).build();

        when(bookRepository.findById(bookId)).thenReturn(book);
        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        assertThrows(IllegalArgumentException.class, () -> {orderService.makeOrder(userEmail, bookId, quantity);});
    }

    @Test
    public void throw_error_if_book_is_not_found() {
        int bookId = 1;
        int quantity = 1;
        String userEmail = "savitar@gmail.com";

        User user = User.builder().email(userEmail).build();
        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        when(bookRepository.findById(bookId)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {orderService.makeOrder(userEmail, bookId, quantity);});
    }

    @Test
    public void throw_error_if_user_is_not_found() {
        int bookId = 1;
        int quantity = 1;
        String userEmail = "savitar@gmail.com";

        when(userRepository.findByEmail(userEmail)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {orderService.makeOrder(userEmail, bookId, quantity);});
    }

    @Test
    public void return_new_order_if_everything_is_valid() {
        int bookId = 1;
        int quantity = 1;
        int bookPrice = 100;
        int stockQuantity = 10;
        String userEmail = "savitar@gmail.com";

        User user = User.builder().email(userEmail).build();
        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        Book book = Book.builder().id(bookId).stockQuantity(stockQuantity).price(bookPrice).build();
        when(bookRepository.findById(bookId)).thenReturn(book);

        Order expected = Order.builder().userEmail(userEmail).bookId(bookId).quantity(quantity).totalPrice(quantity * bookPrice).build();

        Order actual = orderService.makeOrder(userEmail, bookId, quantity);

        assertEquals(expected, actual);
    }

    @Test
    void should_cancel_order_and_restore_stock_quantity() {
        long orderId = 1L;
        long bookId = 1L;
        int quantity = 5;
        int initialStock = 10;
        String userEmail = "savitar@gmail.com";

        Order order = Order.builder()
                .id(orderId)
                .userEmail(userEmail)
                .bookId(bookId)
                .quantity(quantity)
                .build();
        Book book = Book.builder()
                .id(bookId)
                .stockQuantity(initialStock)
                .build();

        when(orderRepository.findById(orderId)).thenReturn(order);
        when(bookRepository.findById(bookId)).thenReturn(book);
        doNothing().when(orderRepository).delete(order);

        orderService.cancelOrder(orderId);

        assertEquals(initialStock + quantity, book.getStockQuantity()); // Kiểm tra stock được cộng lại
        verify(bookRepository).save(book);
        verify(orderRepository).delete(order);
    }
}
