package com.phamhieu.bookapi.persistence.book;

import com.phamhieu.bookapi.domain.book.Book;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@UtilityClass
public class BookEntityMapper {

    public static Book toBook(final BookEntity bookEntity) {
        return Book.builder()
                .id(bookEntity.getId())
                .title(bookEntity.getTitle())
                .author(bookEntity.getAuthor())
                .description(bookEntity.getDescription())
                .createdAt(bookEntity.getCreatedAt())
                .updatedAt(bookEntity.getUpdatedAt())
                .image(bookEntity.getImage())
                .subtitle(bookEntity.getSubtitle())
                .publisher(bookEntity.getPublisher())
                .isbn13(bookEntity.getIsbn13())
                .price(bookEntity.getPrice())
                .year(bookEntity.getYear())
                .rating(bookEntity.getRating())
                .userId(bookEntity.getUserId())
                .build();
    }

    public static List<Book> toBooks(final List<BookEntity> bookEntities) {
        return emptyIfNull(bookEntities)
                .stream()
                .map(BookEntityMapper::toBook)
                .toList();
    }

    public static BookEntity toBookEntity(final Book book) {
        return BookEntity.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .description(book.getDescription())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .image(book.getImage())
                .subtitle(book.getSubtitle())
                .publisher(book.getPublisher())
                .isbn13(book.getIsbn13())
                .price(book.getPrice())
                .year(book.getYear())
                .rating(book.getRating())
                .userId(book.getUserId())
                .build();
    }
}

