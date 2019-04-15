package com.jackeri.school.mailprank.smtp;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Represents a SMTP client used to send mails.
 *
 * @author Nohan Budry, Andrés Moreno
 */
public class SmtpClient implements ISmtpClient {

    private static Logger LOG = Logger.getLogger(SmtpClient.class.getSimpleName());

    private final String HOST;
    private final int PORT;
    private final String USERNAME;
    private final String PASSWORD;
    private final int COOLDOWN;

    private Socket socket = null;
    private PrintWriter writer = null;
    private BufferedReader reader = null;

    /**
     * Creates a new SMTP client.
     * @param host the host
     * @param port the port
     * @param username the username
     * @param password the password
     * @param cooldown the cooldonwn between each mails
     */
    public SmtpClient(String host, int port, String username, String password, int cooldown) {
        HOST = host;
        PORT = port;
        USERNAME = username;
        PASSWORD = password;
        COOLDOWN = cooldown;
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

        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        // clear socket and streams
        if (checkResponseCode(getResponses().getLast(), "220")) {
            return true;
        }

        LOG.severe("Failed opening the socket!");

        closeSocket();
        return false;
    }

    /**
     * Closes the socket and the I/O streams
     *
     * @throws IOException if an I/O exception occurred when closing the socket and the streams
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
     * @throws IOException if there is a socket or a stream error
     */
    private LinkedList<String> getResponses() throws IOException {
        LinkedList<String> responses = new LinkedList<String>();

        do {
            responses.add(reader.readLine());
        } while (!isLastResponse(responses.getLast()));

        return responses;
    }

    /**
     * Sends a mail to each receivers.
     *
     * @param mail mail to send
     * @throws IOException if there is a socket or a stream error
     */
    public void sendMessage(Mail mail) throws IOException {

        if (!openSocket()) {
            return;
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
                LOG.severe(response);
                closeSocket();
                return;
            }
        }

        LOG.info(String.format("Sending mail: \"%s\"", mail.getSubject()));

        // Try to authenticate if needed
        if (authResponse != null && !authLogin()) {
            closeSocket();
            return;
        }

        if (sendMail(mail)) {
            LOG.info(String.format("Mail sent"));
        }

        // Close connection
        writer.printf("QUIT");
        closeSocket();
    }

    /**
     * Try to authenticate to the smtp connectio using AUTH LOGIN.
     * The username and the password must be base64 encoded.
     *
     * @return true if the authentication succeeded
     * @throws IOException if there is a socket or a stream error
     */
    private boolean authLogin() throws IOException {

        writer.printf("AUTH LOGIN\r\n");

        if (checkResponseCode(getResponses().getLast(), "334")) {

            writer.printf("%s\r\n", USERNAME);

            if (checkResponseCode(getResponses().getLast(), "334")) {
                writer.printf("%s\r\n", PASSWORD);

                if (checkResponseCode(getResponses().getLast(), "235")) {
                    LOG.info("Authentication succeeded!");
                    return true;
                }
            }
        }

        LOG.info("Authentication failed!");
        return false;
    }

    /**
     * Sends the given mail to all receivers
     *
     * @return returns the numbers receivers that were sent a message
     * @throws IOException if there is a socket or a stream error
     */
    private boolean sendMail(Mail mail) throws IOException {

        String response;

        // Set sender
        writer.printf("MAIL FROM: <%s>\r\n", mail.getSender());

        response = getResponses().getLast();
        if (!checkResponseCode(response, "250")) {
            LOG.severe(response);
            return false;
        }

        // Set receivers
        for (String receiver : mail.getReceivers()) {
            writer.printf("RCPT TO: <%s>\r\n", receiver);

            response = getResponses().getLast();
            if (!checkResponseCode(response, "250")) {
                LOG.severe(response);
                return false;
            }
        }

        // send data
        writer.printf("DATA\r\n");

        response = getResponses().getLast();
        if (!checkResponseCode(response, "354")) {
            LOG.severe(response);
            return false;
        }

        StringBuilder receivers = new StringBuilder();
        for (int i = 0; i < mail.getReceivers().length; ++i) {
            if (i > 0) {
                receivers.append(",");
            }
            receivers.append(mail.getReceivers()[i]);
        }

        // header
        writer.printf("From: %s\r\n", mail.getSender());
        writer.printf("To: %s\r\n", receivers);
        writer.printf("Subject: %s\r\n", mail.getSubject());

        // separator
        writer.printf("\r\n");

        // body
        writer.printf("%s\r\n", mail.getBody());

        // end
        writer.printf(".\r\n");

        response = getResponses().getLast();
        if (!checkResponseCode(response, "250")) {
            LOG.severe(response);
            return false;
        }

        LOG.info(String.format(
                "Mail: sent from %s", mail.getSender()
        ));

        // Wait before sending the next mail.
        if (COOLDOWN >= 0) {
            try {
                Thread.sleep(COOLDOWN);
            } catch (InterruptedException e) {
                LOG.severe(String.format("Failed wait for %d ms.", COOLDOWN));
            }
        }

        return true;
    }
}
