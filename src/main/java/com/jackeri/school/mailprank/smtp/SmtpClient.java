package com.jackeri.school.mailprank.smtp;


import com.jackeri.school.mailprank.Mail;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class SmtpClient implements ISmtpClient {

    private final String CHARSET = "UTF-8";
    private final String HOST;
    private final int PORT;
    private final String USERNAME;
    private final String PASSWORD;

    private Socket socket = null;
    private PrintWriter writer = null;
    private BufferedReader reader = null;

    public SmtpClient(String host, int port, String username, String password) {
        HOST = host;
        PORT = port;
        USERNAME = username;
        PASSWORD = password;
    }

    /**
     * Connects a socket to a smtp server.
     *
     * @return true if the socket is ready
     * @throws IOException
     */
    private boolean openSocket() throws IOException {

        if (socket != null) {
            closeSocket();
        }

        socket = new Socket(HOST, PORT);

        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

        // clear socket and streams
        if (checkResponseCode(getResponses().getLast(), "220")) {
            return true;
        }

        closeSocket();
        return true;
    }

    /**
     * Closes the socket and the I/O streams
     *
     * @throws IOException if an I/O exception occured when cosing the socket and the streams
     */
    private void closeSocket() throws IOException {

        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        socket = null;

        writer.close();
        writer = null;

        reader.close();
        reader = null;
    }

    /**
     * Checks if the code of the response corresponds to the expected code
     *
     * @param response     String with the smtp response
     * @param expectedCode String with the expected response Code
     * @return true is the codes are equals.
     */
    private boolean checkResponseCode(String response, String expectedCode) {
        return response.length() >= 3 && response.substring(0, 3).equals(expectedCode);
    }

    /**
     * Check is a response is the last response.
     *
     * @param response String with the smtp response
     * @return true if it's the last one
     */
    private boolean isLastResponse(String response) {
        return response.length() >= 4 && response.charAt(3) == ' ';
    }

    /**
     * Gets the responses in a linked list of strings
     *
     * @return A LinkedList with the responses
     * @throws IOException
     */
    private LinkedList<String> getResponses() throws IOException {
        LinkedList<String> responses = new LinkedList<String>();

        do {
            responses.add(reader.readLine());
        } while (!isLastResponse(responses.getLast()));

        return responses;
    }

    /**
     * Sends a mail.
     *
     * @param mail mail to send
     * @return true is the mail has been correctly sent.
     * @throws IOException
     */
    public boolean sendMessage(Mail mail) throws IOException {

        if (!openSocket()) {
            return false;
        }

        // Ask to write mail
        writer.printf("EHLO mailprank\r\n");

        // Check responses and if it should authenticate
        String authResponse = null;
        LinkedList<String> responses = getResponses();
        for (String response : responses) {
            if (checkResponseCode(response, "250")) {

                if (response.contains("AUTH")) {
                    authResponse = response;
                }

            } else {
                return false;
            }
        }

        if (authResponse != null && !authLogin()) {
            return false;
        }

        boolean mailSent = sendMail(mail);

        // Close connection
        writer.printf("QUIT");
        closeSocket();

        return mailSent;
    }

    /**
     *
     * @return
     * @throws IOException
     */
    private boolean authLogin() throws IOException {

        writer.printf("AUTH LOGIN\r\n");

        if (checkResponseCode(getResponses().getLast(), "334")) {

            writer.printf("%s\r\n", USERNAME);

            if (checkResponseCode(getResponses().getLast(), "334")) {
                writer.printf("%s\r\n", PASSWORD);

                if (checkResponseCode(getResponses().getLast(), "235")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Sends a single mail.
     *
     * @param mail mail to send
     * @return true if the mail has been sent
     * @throws IOException
     */
    private boolean sendMail(Mail mail) throws IOException {

        // Set sender
        writer.printf("MAIL FROM: <%s>\r\n", mail.getSender());

        if (!checkResponseCode(getResponses().getLast(), "250")) {
            return false;
        }

        // Set receiver
        writer.printf("RCPT TO: <%s>\r\n", mail.getReceiver());

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

        return true;
    }
}
