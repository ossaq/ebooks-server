package com.example.ebooxapi.controller;

import com.example.ebooxapi.data.BookDTO;
import com.example.ebooxapi.model.Book;
import com.example.ebooxapi.repository.BookRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api-v1/book")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping("/put")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookDTO bookDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.ofNullable(String.join(", ", bindingResult.getAllErrors().stream().map(Object::toString)
                    .collect(Collectors.joining(", "))));
        }

        bookRepository.save(Book.builder().title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .genre(bookDTO.getGenre())
                .build());

        return ResponseEntity.ok("Book was saved");
    }

    @PostMapping("/delete/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable String bookId){
        if(bookRepository.findById(Integer.valueOf(bookId)).isEmpty()){
            ResponseEntity.ofNullable("Unknown book");
        }
        bookRepository.deleteById(Integer.valueOf(bookId));

        return ResponseEntity.ok("Book was deleted");
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllBooks(){
        return new ResponseEntity<>(bookRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/modify/{bookId}")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookDTO bookDTO, @PathVariable String bookId){
        bookRepository.save(Book.builder().title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .genre(bookDTO.getGenre())
                .id(Integer.valueOf(bookId))
                .build());

        return ResponseEntity.ok("Book was updated");
    }

    @GetMapping("/get/{bookId}")
    public ResponseEntity<?> getAllBooks(@PathVariable String bookId){
        return new ResponseEntity<>(bookRepository.findById(Integer.valueOf(bookId)), HttpStatus.OK);
    }
}
