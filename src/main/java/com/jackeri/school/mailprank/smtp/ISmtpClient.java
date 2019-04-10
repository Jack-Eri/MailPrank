package com.jackeri.school.mailprank.smtp;

import com.jackeri.school.mailprank.Mail;

import java.io.IOException;

public interface ISmtpClient {

    boolean sendMessage(Mail mail) throws IOException;

    void sendMessages(Mail[] mails) throws  IOException;
}
