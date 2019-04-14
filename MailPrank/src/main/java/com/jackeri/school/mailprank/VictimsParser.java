package com.jackeri.school.mailprank;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

/**
 * Reads the file containing the victims e-mail addresses.
 * The file should contain one address per line.
 *
 * @author Nohan Budry, Adnrés Moreno
 */
public class VictimsParser {

    private BufferedReader reader;
    private LinkedList<Victim> victims;

    /**
     * Parses the file to a linked list of victims
     * @param filename name of the file containing the victims
     */
    public VictimsParser(String filename) {

        victims = new LinkedList<>();

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.out.println("Failed opening " + filename + "!");
            System.exit(-1);
        }

        // Read victims
        Victim victim;
        while ((victim = readNextVictim()) != null) {
            victims.add(victim);
        }
    }

    /**
     * Reads te next victim in the file. There is one victim per line.
     *
     * @return The victim or null if there are no more victims
     */
    private Victim readNextVictim() {

        String mail;
        try {
            mail = reader.readLine();
        } catch (IOException e) {
            return null;
        }

        if (mail == null) {
            return null;
        }

        return new Victim(mail);
    }

    /**
     * Returns the list of victims.
     * @return a linked list of victims
     */
    public LinkedList<Victim> getVictims() {
        return victims;
    }
}
