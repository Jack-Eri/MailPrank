package com.jackeri.school.mailprank.smtp;

/**
 * Represents a mail that can be sent by the SMTP client.
 *
 * @author Nohan Budry, Andrés Moreno
 */
public class Mail {

    private String sender;
    private String[] receivers;
    private String subject;
    private String body;

    /**
     * Create a mail.
     *
     * @param sender    the sender's e-mail adress
     * @param receivers the e-mail adresses of the receivers
     * @param subject   the mail's subject
     * @param body      the mail's body
     */
    public Mail(String sender, String[] receivers, String subject, String body) {
        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.body = body;
    }

    /**
     * Returns the sender of the mail
     *
     * @return the mail sender's address
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns the list of the receivers' e-mail addresses.
     *
     * @return an array with the  e-mail addresses ot the reseivers
     */
    public String[] getReceivers() {
        return receivers;
    }

    /**
     * Returns the subject of the mail.
     *
     * @return the mail's subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Returns the body of the mail.
     *
     * @return the mail's body
     */
    public String getBody() {
        return body;
    }
}
