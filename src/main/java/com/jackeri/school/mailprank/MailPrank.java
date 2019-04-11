package com.jackeri.school.mailprank;

import com.jackeri.school.mailprank.smtp.SmtpClient;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

public class MailPrank {

    Properties properties;
    SmtpClient smtpClient;
    VictimGroup[] victimGroups;

    public MailPrank(String propertiesFileName, Collection<Victim> victims) {

        this.properties = new Properties();
        this.victimGroups = new VictimGroup[1];

        loadProperties(propertiesFileName);

        // Create smtp client
        smtpClient = new SmtpClient(
                properties.getProperty("smtp-host"),
                Integer.parseInt(properties.getProperty("smtp-port"))
        );

        createGroups(victims);

        victimGroups[0].sendMails(smtpClient);
    }

    /**
     * Loads properties file.
     * @param filename properties file name
     */
    private void loadProperties(String filename) {

        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(filename), "UTF-8");

            properties.load(reader);

        } catch (IOException e) {
            System.out.println("Failed loading properties: " + filename + "!");
            System.exit(-1);
        }
    }

    //
    private void createGroups(Collection<Victim> victims) {

        // Create groups
        Iterator<Victim> it = victims.iterator();
        victimGroups[0] = new VictimGroup(it.next());
        while (it.hasNext()) {
            victimGroups[0].addReceiver(it.next());
        }
    }

    public static void main(String[] args) {

        LinkedList<Victim> victims = new LinkedList<Victim>();

        // Create emails reader
        String emailsFileName = "emails.txt";
        try {

            BufferedReader victimsReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(emailsFileName), "UTF-8")
            );

            // Read emails and create victims
            String victimMail;
            while (((victimMail = victimsReader.readLine()) != null)) {
                if (victimMail.length() > 0) {
                    victims.add(new Victim(victimMail));
                }
            }
        } catch (IOException e) {
            System.out.println("Error found while reading emails!");
            return;
        }

        new MailPrank("config.properties", victims);
    }
}
