package com.jackeri.school.mailprank;

/**
 * Represents a victim of a prank. Contains the e-mail address of the victim.
 *
 * @author Nohan Budry, Andrés Moreno
 */
public class Victim {

    private String mail;

    /**
     * Creates a new victim.
     * @param mail the e-mail address
     */
    public Victim(String mail) {
        this.mail = mail;
    }

    /**
     * Returns the prank's subject.
     * @return the prank's subject
     */
    public String getMail() {
        return mail;
    }
}
