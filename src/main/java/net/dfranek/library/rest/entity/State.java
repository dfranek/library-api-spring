package net.dfranek.library.rest.entity;

import net.dfranek.library.rest.dto.StateDto;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class State implements EntityInterface<StateDto> {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private BookState state;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private LocalDate dateStarted;

    private LocalDate dateCompleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BookState getState() {
        return state;
    }

    public void setState(BookState state) {
        this.state = state;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
    public StateDto toDto() {
        StateDto stateDto = new StateDto();
        stateDto.setDateCompleted(dateCompleted);
        stateDto.setDateStarted(dateStarted);
        stateDto.setState(state);
        return stateDto;
    }
}
