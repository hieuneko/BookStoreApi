package com.phamhieu.bookapi.domain.book;

import com.phamhieu.bookapi.error.BadRequestException;
import com.phamhieu.bookapi.error.NotFoundException;
import com.phamhieu.bookapi.persistence.book.BookStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static com.phamhieu.bookapi.fakes.BookFakes.buildBook;
import static com.phamhieu.bookapi.fakes.BookFakes.buildBooks;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookStore bookStore;

    @InjectMocks
    private BookService bookService;

    @Test
    void shouldFindAll_OK() {

        final var expected = buildBooks();

        when(bookStore.findAll())
                .thenReturn(expected);

        final var actual = bookService.findAll();

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
        assertEquals(expected.get(0).getAuthor(), actual.get(0).getAuthor());
        assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
        assertEquals(expected.get(0).getCreatedAt(), actual.get(0).getCreatedAt());
        assertEquals(expected.get(0).getUpdatedAt(), actual.get(0).getUpdatedAt());
        assertEquals(expected.get(0).getImage(), actual.get(0).getImage());
        assertEquals(expected.get(0).getUserId(), actual.get(0).getUserId());

        verify(bookStore).findAll();
    }

    @Test
    void shouldFindById_OK() {
        final var expected = buildBook();
        when(bookStore.findById(expected.getId()))
                .thenReturn(Optional.of(expected));

        assertEquals(expected, bookService.findById(expected.getId()));
        verify(bookStore).findById(expected.getId());
    }

    @Test
    void shouFindById_Throw() {
        final var id = randomUUID();
        when(bookStore.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.findById(id));
        verify(bookStore).findById(id);
    }

    @Test
    void shouldFind_OK() {
        final var book = buildBook();
        final var expected = buildBooks();

        when(bookStore.find(anyString())).thenReturn(expected);

        final var actual = bookService.find(anyString());

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(0).getTitle(), actual.get(0).getTitle());
        assertEquals(expected.get(0).getAuthor(), actual.get(0).getAuthor());
        assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
        assertEquals(expected.get(0).getCreatedAt(), actual.get(0).getCreatedAt());
        assertEquals(expected.get(0).getUpdatedAt(), actual.get(0).getUpdatedAt());
        assertEquals(expected.get(0).getImage(), actual.get(0).getImage());
        assertEquals(expected.get(0).getUserId(), actual.get(0).getUserId());

        verify(bookStore).find(anyString());
    }

    @Test
    void shouldFind_Empty() {
        final var title = randomAlphabetic(3, 10);

        when(bookStore.find(title)).thenReturn(Collections.emptyList());

        assertTrue(bookService.find(title).isEmpty());
        verify(bookStore).find(title);
    }

    @Test
    void shouldCreate_OK() {
        final var book = buildBook();

        when(bookStore.create(any())).thenReturn(book);

        final var actual = bookService.create(book);

        assertEquals(book.getId(), actual.getId());
        assertEquals(book.getTitle(), actual.getTitle());
        assertEquals(book.getAuthor(), actual.getAuthor());
        assertEquals(book.getDescription(), actual.getDescription());
        assertEquals(book.getCreatedAt(), actual.getCreatedAt());
        assertEquals(book.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(book.getImage(), actual.getImage());
        assertEquals(book.getUserId(), actual.getUserId());

        verify(bookStore).create(any());
    }

    @Test
    void shouldCreate_ThrowBadRequest() {
        final var expected = buildBook();
        expected.setTitle(null);

        assertThrows(BadRequestException.class, () -> bookService.create(expected));
    }

    @Test
    void shouldUpdate_OK() {
        final var book = buildBook();
        final var updatedBook = buildBook();
        updatedBook.setId(book.getId());

        when(bookStore.findById((book.getId()))).thenReturn(Optional.of(book));
        when(bookStore.update(book)).thenReturn(book);

        final var actual = bookService.update(book.getId(), updatedBook);

        assertEquals(book.getId().toString(), actual.getId().toString());
        assertEquals(book.getTitle(), actual.getTitle());
        assertEquals(book.getAuthor(), actual.getAuthor());
        assertEquals(book.getDescription(), actual.getDescription());
        assertEquals(book.getCreatedAt(), actual.getCreatedAt());
        assertEquals(book.getUpdatedAt(), actual.getUpdatedAt());
        assertEquals(book.getImage(), actual.getImage());
        assertEquals(book.getUserId().toString(), actual.getUserId().toString());

        verify(bookStore).update(book);
    }

    @Test
    void shouldUpdate_NotFound() {
        final var bookUpdate = buildBook();
        final var uuid = randomUUID();

        when(bookStore.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.update(uuid, bookUpdate));
        verify(bookStore).findById(uuid);
    }

    @Test
    void shouldDeleteById_Ok() {
        final var book = buildBook();

        bookService.delete(book.getId());
        verify(bookStore).delete(book.getId());
    }

    @Test
    void shouldDeleteById_NotFound() {
        final var id = randomUUID();

        when(bookStore.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.findById(id));
        verify(bookStore).findById(id);
    }
}