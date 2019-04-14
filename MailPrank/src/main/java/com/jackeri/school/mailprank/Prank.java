package com.jackeri.school.mailprank;

/**
 * Represents a prank. Contains the subject and the massage of the prank.
 *
 * @author Nohan Budry, Andrés Moreno
 */
public class Prank {

    private String subject;
    private String message;

    /**
     * Creates new prank.
     * @param subject the prank's subject
     * @param message the prank's message
     */
    public Prank(String subject, String message) {
        this.subject = subject;
        this.message = message;
    }

    /**
     * Returns the prank's subject.
     * @return the prank's subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Returns the prank's message.
     * @return the prank's message
     */
    public String getMessage() {
        return message;
    }
}
