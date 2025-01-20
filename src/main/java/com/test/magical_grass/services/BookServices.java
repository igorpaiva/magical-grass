package com.test.magical_grass.services;

import com.test.magical_grass.controllers.BookController;
import com.test.magical_grass.dto.BookDTO;
import com.test.magical_grass.exceptions.RequiredObjectIsNullException;
import com.test.magical_grass.exceptions.ResourceNotFoundException;
import com.test.magical_grass.mapper.ModelMapperWrapper;
import com.test.magical_grass.model.Book;
import com.test.magical_grass.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {
    private Logger logger = Logger.getLogger(BookServices.class.getName());
    @Autowired
    BookRepository bookRepository;

    @Autowired
    PagedResourcesAssembler<BookDTO> pagedResourcesAssembler;

    public PagedModel<EntityModel<BookDTO>> findAll(Pageable pageable) {
        logger.info("Finding all books");
        Page<Book> bookPage = bookRepository.findAll(pageable);
        Page<BookDTO> bookDTOPage = bookPage.map(book -> ModelMapperWrapper.parseObject(book, BookDTO.class));
        bookDTOPage.map(bookDTO -> bookDTO.add(linkTo(methodOn(BookController.class)
                .findById(bookDTO.getKey())).withSelfRel()));
        Link link = linkTo(methodOn(BookController.class)
                .findAll(pageable.getPageNumber(), pageable.getSort().toString(), pageable.getPageSize())).withSelfRel();
        return pagedResourcesAssembler.toModel(bookDTOPage, link);
    }

    public BookDTO findById(Long id) {
        Book foundBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        BookDTO bookDTO = ModelMapperWrapper.parseObject(foundBook, BookDTO.class);
        bookDTO.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return bookDTO;
    }

    public BookDTO createBook(BookDTO book) {
        if (book == null) throw new RequiredObjectIsNullException();
        logger.info("Creating book: " + book);
        Book createdBook = ModelMapperWrapper.parseObject(book, Book.class);
        BookDTO createdBookDTO = ModelMapperWrapper.parseObject(bookRepository.save(createdBook), BookDTO.class);
        createdBookDTO.add(linkTo(methodOn(BookController.class).findById(createdBookDTO.getKey())).withSelfRel());
        return createdBookDTO;
    }

    public BookDTO updateBook(BookDTO book, Long id) {
        if (book == null) throw new RequiredObjectIsNullException();
        logger.info("Updating book: " + book);
        Book updatedBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));

        updatedBook.setId(id);
        updatedBook.setTitle(book.getTitle());
        updatedBook.setAuthor(book.getAuthor());
        updatedBook.setPrice(book.getPrice());
        updatedBook.setLaunchDate(book.getLaunchDate());

        BookDTO bookDTO = ModelMapperWrapper.parseObject(bookRepository.save(updatedBook), BookDTO.class);
        bookDTO.add(linkTo(methodOn(BookController.class).findById(bookDTO.getKey())).withSelfRel());
        return bookDTO;
    }

    public void deleteBook(Long id) {
        logger.info("Deleting book: " + id);
        Book deleteBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID."));
        bookRepository.delete(deleteBook);
    }
}
