package com.example.springtest.service;

import com.example.springtest.model.Book;
import com.example.springtest.model.Cart;
import com.example.springtest.model.CartItem;
import com.example.springtest.model.User;
import com.example.springtest.repository.IBookRepository;
import com.example.springtest.repository.ICartRepository;
import com.example.springtest.repository.IUserRepository;

import java.util.List;

public class CartService {
    ICartRepository cartRepository;
    IUserRepository userRepository;
    IBookRepository bookRepository;
    public CartService(ICartRepository cartRepository, IUserRepository userRepository, IBookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public Cart updateCart(List<CartItem> carts, String userEmail) {
        // check userEmail valid
        User user = userRepository.findByEmail(userEmail);
        if (user == null) throw new IllegalArgumentException("Không tìm thấy User!");

        if (carts == null || carts.isEmpty()) throw new IllegalArgumentException("Giỏ hàng trống");

        for (CartItem cartItem : carts) {
            long bookId = cartItem.getBookId();
            int quantity = cartItem.getQuantity();

            if (quantity <= 0) {
                throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
            }

            Book book = bookRepository.findById(bookId);

            if (book == null) throw new IllegalArgumentException("Không tìm thấy sách");

            if (quantity > book.getStockQuantity()) {
                cartItem.setQuantity(book.getStockQuantity()); // chỉnh lại số lượng = toofn kho
            }
        }

        Cart cart = cartRepository.findByUserEmail(userEmail);

        if (cart == null)  {
            cart = Cart.builder().userEmail(userEmail).cartItemList(carts).build();
            cartRepository.save(cart);
            return cart;
        }

        cart.setCartItemList(carts);
        cartRepository.save(cart);
        return cart;
    }
}
