package com.jackeri.school.mailprank.smtp;

import com.jackeri.school.mailprank.Mail;

import java.io.IOException;

public interface ISmtpClient {

    void sendMessage(Mail mail) throws IOException;
}
