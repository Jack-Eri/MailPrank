package com.jackeri.school.mailprank.smtp;

public class Mail {

    private String sender;
    private String[] receivers;
    private String subject;
    private String body;

    public Mail(String sender, String[] receivers, String subject, String body) {
        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public String[] getReceivers() {
        return receivers;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
