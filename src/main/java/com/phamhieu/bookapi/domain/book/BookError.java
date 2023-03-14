package com.phamhieu.bookapi.domain.book;

import com.phamhieu.bookapi.error.BadRequestException;
import com.phamhieu.bookapi.error.NotFoundException;
import lombok.experimental.UtilityClass;

import java.util.UUID;
import java.util.function.Supplier;

@UtilityClass
public class BookError {

    public static Supplier<NotFoundException> supplyBookNotFoundById(final UUID id) {
        return () -> new NotFoundException("Book has id: %s couldn't be found", id);
    }

    public static Supplier<BadRequestException> supplyNotEnoughInformation(final String field) {
        return () -> new BadRequestException("Information isn't not enough, " + field + " must be required");
    }
}
