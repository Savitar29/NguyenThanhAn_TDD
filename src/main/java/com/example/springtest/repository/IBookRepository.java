package com.example.springtest.repository;

import com.example.springtest.model.Book;

public interface IBookRepository {
    Book findById(long id);

    Book save(Book book);
}
