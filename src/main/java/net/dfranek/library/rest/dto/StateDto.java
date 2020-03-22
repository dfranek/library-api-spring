package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.dfranek.library.rest.entity.BookState;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateDto {

    private BookState state;
    private ZonedDateTime dateStarted;
    private ZonedDateTime dateCompleted;

    public BookState getState() {
        return state;
    }

    public void setState(BookState state) {
        this.state = state;
    }

    public ZonedDateTime getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(ZonedDateTime dateStarted) {
        this.dateStarted = dateStarted;
    }

    public ZonedDateTime getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(ZonedDateTime dateCompleted) {
        this.dateCompleted = dateCompleted;
    }
}
