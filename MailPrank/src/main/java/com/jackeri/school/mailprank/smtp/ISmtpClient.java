package com.jackeri.school.mailprank.smtp;

import java.io.IOException;

public interface ISmtpClient {

    void sendMessage(Mail mail) throws IOException;
}
