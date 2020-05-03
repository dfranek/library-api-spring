package net.dfranek.library.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.dfranek.library.rest.entity.BookState;
import net.dfranek.library.rest.entity.State;

import java.time.LocalDate;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateDto implements DtoInterface<State> {

    private BookState state;
    private LocalDate dateStarted;
    private LocalDate dateCompleted;

    public BookState getState() {
        return state;
    }

    public void setState(BookState state) {
        this.state = state;
    }

    public LocalDate getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(LocalDate dateStarted) {
        this.dateStarted = dateStarted;
    }

    public LocalDate getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(LocalDate dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateDto stateDto = (StateDto) o;
        return state == stateDto.state &&
                Objects.equals(dateStarted, stateDto.dateStarted) &&
                Objects.equals(dateCompleted, stateDto.dateCompleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, dateStarted, dateCompleted);
    }

    @Override
    public State toEntity() {
        State stateEntity = new State();
        stateEntity.setDateCompleted(dateCompleted);
        stateEntity.setDateStarted(dateStarted);
        stateEntity.setState(state);
        return stateEntity;
    }
}
