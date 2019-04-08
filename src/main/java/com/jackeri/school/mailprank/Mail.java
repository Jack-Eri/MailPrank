package com.jackeri.school.mailprank;

public class Mail {

    private String sender;
    private String receiver;
    private String subject;
    private String body;

    public Mail(String sender, String receiver, String subject, String body) {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
