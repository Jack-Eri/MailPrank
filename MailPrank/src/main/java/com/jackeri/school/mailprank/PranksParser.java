package com.jackeri.school.mailprank;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

/**
 * Reads the file containing the pranks .
 * Each pranks should be separated be a separator (in one line).
 * The subject is the first line, the message starts at the third line and the second one is ignored.
 *
 * @author Nohan Budry, Adnrés Moreno
 */
public class PranksParser {

    private final String separator;
    private BufferedReader reader;
    private LinkedList<Prank> pranks;

    /**
     * Parses the file containing the pranks to a list of pranks
     *
     * @param filename  name of the file containing the pranks
     * @param separator string used to separate each pranks (in one line)
     */
    public PranksParser(String filename, String separator) {

        this.separator = separator;
        this.pranks = new LinkedList<>();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Failed opening " + filename + "!");
            System.exit(-1);
        }

        // Read pranks
        Prank prank;
        while ((prank = readNextPrank()) != null) {
            pranks.add(prank);
        }
    }

    /**
     * Reads te next prank in the file.
     * Pranks are separated by 'separator'.
     * The first line is the subject of the prank and the second line is ignored.
     *
     * @return The prank or null if there are no more pranks
     */
    private Prank readNextPrank() {

        String subject = null;
        StringBuilder messageBuilder = new StringBuilder();

        String line = null;
        try {
            while (((line = reader.readLine()) != null)) {

                // Done reading prank
                if (line.equals(separator)) {
                    break;
                }

                // the current line is the subject
                if (subject == null) {

                    subject = line;
                    // Ignore next line
                    reader.readLine();

                } else { // the current line is part of the message
                    messageBuilder.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            return null;
        }

        // No more pranks
        if (line == null && subject == null) {
            return null;
        }

        return new Prank(subject, messageBuilder.toString());
    }

    /**
     * Returns the list of pranks.
     *
     * @return linked list of pranks
     */
    public LinkedList<Prank> getPranks() {
        return pranks;
    }
}
