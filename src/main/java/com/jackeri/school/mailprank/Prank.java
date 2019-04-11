package com.jackeri.school.mailprank;

public class Prank {

    private String subject;
    private String message;

    public Prank(String subject, String message) {
        this.subject = subject;
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }
}
