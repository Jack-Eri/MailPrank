package com.jackeri.school.mailprank;

public class Victim {

    private String mail;

    public Victim(String mail) {
        this.mail = mail;
    }

    @Override
    public String toString() {
        return mail;
    }
}
