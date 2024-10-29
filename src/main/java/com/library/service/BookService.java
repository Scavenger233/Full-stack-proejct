package com.library.service;

import java.util.List;

import com.library.model.Book;

public interface BookService {

	List<Book> getAllBooks(String username);
	
	Book getBook(String username, long id);
	
	void deleteBook(String username, long id);
	
	Book updateBook(String username, long id, Book book);
	
	Book createBook(String username, Book book);
}
