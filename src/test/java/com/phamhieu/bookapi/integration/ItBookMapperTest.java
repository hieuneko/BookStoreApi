package com.phamhieu.bookapi.integration;

import org.junit.jupiter.api.Test;

import static com.phamhieu.bookapi.fakes.FetchBookFakes.buildBookItemDetailDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ItBookMapperTest {

    @Test
    void shouldToBookFromItemDetail_OK() {
        final var bookItemDetailDTO = buildBookItemDetailDTO();
        final var book = ItBookMapper.toBook(bookItemDetailDTO);

        assertEquals(bookItemDetailDTO.getTitle(), book.getTitle());
        assertEquals(bookItemDetailDTO.getSubtitle(), book.getSubtitle());
        assertEquals(bookItemDetailDTO.getAuthors(), book.getAuthor());
        assertEquals(bookItemDetailDTO.getPublisher(), book.getPublisher());
        assertEquals(bookItemDetailDTO.getIsbn13(), book.getIsbn13());
        assertEquals(bookItemDetailDTO.getYear(), book.getYear());
        assertEquals(bookItemDetailDTO.getRating(), book.getRating());
        assertEquals(bookItemDetailDTO.getDesc(), book.getDescription());
        assertEquals(bookItemDetailDTO.getPrice(), book.getPrice());
        assertEquals(bookItemDetailDTO.getImage(), book.getImage());
    }
}