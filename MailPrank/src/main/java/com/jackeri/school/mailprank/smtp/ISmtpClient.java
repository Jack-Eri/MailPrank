package com.jackeri.school.mailprank.smtp;

import java.io.IOException;

/**
 * Interface representing what a SMTP client can do.
 */
public interface ISmtpClient {

    /**
     * Send the mail from to sender to each receivers.
     * @param mail mail to send
     * @throws IOException if there is a socket or a stream error
     */
    void sendMessage(Mail mail) throws IOException;
}
