package net.dfranek.library.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Entity already exists")
public class ConflictException extends RuntimeException {
    private final int code = HttpStatus.CONFLICT.value();

    private String message;

    public ConflictException(String message) {
        super(message);
        this.message = message;
    }
}
