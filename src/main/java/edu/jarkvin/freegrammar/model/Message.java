package edu.jarkvin.freegrammar.model;

import java.time.LocalDate;
import java.util.Objects;

public class Message {
    private String subject;
    private LocalDate date;

    public Message() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(subject, message.subject) && Objects.equals(date, message.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, date);
    }
}
