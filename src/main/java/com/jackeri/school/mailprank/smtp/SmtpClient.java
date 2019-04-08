package com.jackeri.school.mailprank.smtp;


import com.jackeri.school.mailprank.Mail;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;


public class SmtpClient implements ISmtpClient {

    private final String CHARSET = "UTF-8";
    private final String HOST;
    private final int PORT;

    private Socket socket = null;
    private PrintWriter writer = null;
    private BufferedReader reader = null;

    public SmtpClient(String host, int port) {

        HOST = host;
        PORT = port;
    }

    private boolean openSocket() throws IOException {

        if (socket != null && !socket.isClosed()) {
            socket.close();
        }

        socket = new Socket(HOST, PORT);

        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

        // clear socket and streams
        if (!checkResponseCode(getResponses().getLast(), "220")) {

            socket.close();
            socket = null;

            writer.close();
            writer = null;

            reader.close();
            reader = null;

            return false;
        }

        return true;
    }

    private boolean checkResponseCode(String response, String expectedCode) {

        return response.length() >= 3 && response.substring(0, 3).equals(expectedCode);
    }

    private boolean isLastResponse(String response) {

        return response.length() >= 4 && response.charAt(3) == ' ';
    }

    private LinkedList<String> getResponses() throws IOException {
        LinkedList<String> responses = new LinkedList<String>();

        do {
            responses.add(reader.readLine());
        } while (!isLastResponse(responses.getLast()));

        return responses;
    }

    public boolean sendMessage(Mail mail) throws IOException {

        if (!openSocket()) {
            return false;
        }

        // Ask to write mail
        writer.printf("EHLO mailprank\r\n");

        if (!checkResponseCode(getResponses().getLast(), "250")) {
            return false;
        }

        // Set sender
        writer.printf("MAIL FROM: %s\r\n", mail.getSender());

        if (!checkResponseCode(getResponses().getLast(), "250")) {
            return false;
        }

        // Set receiver
        writer.printf("RCPT TO: %s\r\n", mail.getReceiver());

        if (!checkResponseCode(getResponses().getLast(), "250")) {
            return false;
        }

        // send data
        writer.printf("DATA\r\n");

        if (!checkResponseCode(getResponses().getLast(), "354")) {
            return false;
        }

        // header
        writer.printf("From: %s\r\n", mail.getSender());
        writer.printf("To: %s\r\n", mail.getReceiver());
        writer.printf("Subject: %s\r\n", mail.getSubject());

        // separator
        writer.printf("\r\n");

        // body
        writer.printf("%s\r\n", mail.getBody());

        // end
        writer.printf(".\r\n");

        if (!checkResponseCode(getResponses().getLast(), "250")) {
            return false;
        }

        // quit
        writer.printf("QUIT");

        return true;
    }
}
