package com.phamhieu.bookapi.api.book;

import org.junit.jupiter.api.Test;

import static com.phamhieu.bookapi.api.book.BookDTOMapper.*;
import static com.phamhieu.bookapi.fakes.BookFakes.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class BookDTOMapperTest {

    @Test
    void shouldToBookDTO_OK() {
        final var book = buildBook();
        final var bookDTO = toBookDTO(book);

        assertEquals(bookDTO.getId(), book.getId());
        assertEquals(bookDTO.getTitle(), book.getTitle());
        assertEquals(bookDTO.getAuthor(), book.getAuthor());
        assertEquals(bookDTO.getDescription(), book.getDescription());
        assertEquals(bookDTO.getImage(), book.getImage());
        assertEquals(bookDTO.getUserId(), book.getUserId());
    }

    @Test
    void shouldToBookDTOs_OK() {
        final var books = buildBooks();

        final var bookDTOs = toBookDTOs(books);
        assertEquals(books.size(), bookDTOs.size());
    }

    @Test
    void shouldToBook_OK() {
        final var bookDTO = buildBookDTO();
        final var book = toBook(bookDTO);

        assertEquals(bookDTO.getId(), book.getId());
        assertEquals(bookDTO.getTitle(), book.getTitle());
        assertEquals(bookDTO.getAuthor(), book.getAuthor());
        assertEquals(bookDTO.getDescription(), book.getDescription());
        assertEquals(bookDTO.getImage(), book.getImage());
        assertEquals(bookDTO.getUserId(), book.getUserId());
    }
}