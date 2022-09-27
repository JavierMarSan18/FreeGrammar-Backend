package comedu.jarkvin.freegrammar.model;

import java.time.LocalDate;

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
}
