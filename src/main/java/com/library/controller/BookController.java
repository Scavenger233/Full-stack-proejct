package com.library.controller;

import java.net.URI;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.library.model.Book;
import com.library.service.BookService;

//@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RestController
public class BookController {
	
	@Autowired
	private BookService bookService;

	@GetMapping("/library/{username}/books")
	public List<Book> getAllBooks(@PathVariable String username) {
		
		List<Book> books = bookService.getAllBooks(username);
		
		return books;
	}

	@GetMapping("/library/{username}/books/{id}")
	public Book getBook(@PathVariable String username, @PathVariable long id) {
		
		Book book = bookService.getBook(username, id);
		return book;
	}

	@DeleteMapping("/library/{username}/books/{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable String username, @PathVariable long id) {

		bookService.deleteBook(username, id);

		ResponseEntity<Void> responseEntity = ResponseEntity.noContent().build();
		return responseEntity;
	}

	@PutMapping("/library/{username}/books/{id}")
	public ResponseEntity<Book> updateBook(@PathVariable String username, @PathVariable long id,
			@RequestBody Book book) {

		book.setUsername(username);
		
		Book bookUpdated = bookService.updateBook(username, id, book);
		
		ResponseEntity<Book> responseEntity = new ResponseEntity<Book>(bookUpdated, HttpStatus.OK);

		return responseEntity;
	}

	@PostMapping("/library/{username}/books")
	public ResponseEntity<Void> createBook(@PathVariable String username, @RequestBody Book book) {
		
		book.setUsername(username);

		//TODO add already exists
		//TODO maybe add UI how to handle exceptions
		Book createdBook = bookService.createBook(username, book);
		
		if (createdBook == null)
			return ResponseEntity.noContent().build();

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdBook.getId())
				.toUri(); 

		return ResponseEntity.created(uri).build();
	}

}