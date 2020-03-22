package net.dfranek.library.rest.entity;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
public class State {
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

    private ZonedDateTime dateStarted;

    private ZonedDateTime dateCompleted;

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
