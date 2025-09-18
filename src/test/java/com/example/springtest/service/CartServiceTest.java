package com.example.springtest.service;


import com.example.springtest.model.Book;
import com.example.springtest.model.Cart;
import com.example.springtest.model.CartItem;
import com.example.springtest.model.User;
import com.example.springtest.repository.IBookRepository;
import com.example.springtest.repository.ICartRepository;
import com.example.springtest.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    ICartRepository cartRepository;

    @Mock
    IUserRepository userRepository;

    @Mock
    IBookRepository bookRepository;

    @InjectMocks
    CartService cartService;


    @Test
    public void throw_error_if_user_email_is_invalid() {
        String userEmail = "savitar@gmail.com";
        when(userRepository.findByEmail(userEmail)).thenReturn(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {cartService.updateCart(null, userEmail);});
    }

    @Test
    public void throw_error_if_cart_item_list_is_empty() {
        String userEmail = "savitar@gmail.com";
        when(userRepository.findByEmail(userEmail)).thenReturn(User.builder().email(userEmail).build());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {cartService.updateCart(null, userEmail);});
    }


    @Test
    public void throw_error_if_one_of_the_cart_item_quantity_is_invalid() {
        String userEmail = "savitar@gmail.com";
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(CartItem.builder().bookId(1L).quantity(-1).build());

        when(userRepository.findByEmail(userEmail)).thenReturn(User.builder().email(userEmail).build());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {cartService.updateCart(cartItemList, userEmail);});
    }

    @Test
    public void throw_error_if_one_of_the_cart_item_quantity_is_greater_then_stock_quantity() {
        String userEmail = "savitar@gmail.com";
        long bookId = 1;
        int cartItemQuantity = 10;
        int stockQuantity = 2;

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(CartItem.builder().bookId(1).quantity(cartItemQuantity).build());

        when(userRepository.findByEmail(userEmail)).thenReturn(User.builder().email(userEmail).build());
        when(bookRepository.findById(bookId)).thenReturn(Book.builder().id(bookId).stockQuantity(stockQuantity).build());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {cartService.updateCart(cartItemList, userEmail);});
    }

    @Test
    public void throw_error_if_one_of_the_cart_item_book_id_is_invalid() {
        String userEmail = "savitar@gmail.com";
        long bookId = 1;
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(CartItem.builder().bookId(bookId).quantity(1).build());

        when(bookRepository.findById(bookId)).thenReturn(null);
        when(userRepository.findByEmail(userEmail)).thenReturn(User.builder().email(userEmail).build());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {cartService.updateCart(cartItemList, userEmail);});
    }


    @Test
    public void should_create_new_cart_item_if_user_dont_have_a_cart() {
        String userEmail = "savitar@gmail.com";
        long bookId = 1;
        int cartItemQuantity = 10;
        int stockQuantity = 20;

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(CartItem.builder().bookId(1).quantity(cartItemQuantity).build());

        when(userRepository.findByEmail(userEmail)).thenReturn(User.builder().email(userEmail).build());
        when(bookRepository.findById(bookId)).thenReturn(Book.builder().id(bookId).stockQuantity(stockQuantity).build());

        when(cartRepository.findByUserEmail(userEmail)).thenReturn(null);

        Cart expectedCart = Cart.builder().userEmail(userEmail).cartItemList(cartItemList).build();
        Cart result = cartService.updateCart(cartItemList, userEmail);

        assertEquals(expectedCart, result);
    }

    @Test
    public void should_update_the_cart_if_user_already_have_a_cart() {
        String userEmail = "savitar@gmail.com";
        long bookId = 1;
        int cartItemQuantity = 10;
        int stockQuantity = 20;

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(CartItem.builder().bookId(1).quantity(cartItemQuantity).build());

        when(userRepository.findByEmail(userEmail)).thenReturn(User.builder().email(userEmail).build());
        when(bookRepository.findById(bookId)).thenReturn(Book.builder().id(bookId).stockQuantity(stockQuantity).build());

        when(cartRepository.findByUserEmail(userEmail)).thenReturn(Cart.builder().userEmail(userEmail).cartItemList(null).build());

        Cart expectedCart = Cart.builder().userEmail(userEmail).cartItemList(cartItemList).build();
        Cart result = cartService.updateCart(cartItemList, userEmail);

        assertEquals(expectedCart, result);
    }
}
