package com.phamhieu.bookapi.domain.book;

import lombok.experimental.UtilityClass;

import static com.phamhieu.bookapi.domain.book.BookError.supplyNotEnoughInformation;
import static org.apache.commons.lang3.StringUtils.isBlank;

@UtilityClass
public class BookValidation {
    public void validateBookInformation(final Book book) {
        validateTitle(book.getTitle());
        validateAuthor(book.getAuthor());
    }

    private void validateTitle(final String title) {
        if (isBlank(title)) {
            throw supplyNotEnoughInformation("title").get();
        }
    }

    private void validateAuthor(final String author) {
        if (isBlank(author)) {
            throw supplyNotEnoughInformation("author").get();
        }
    }
}
