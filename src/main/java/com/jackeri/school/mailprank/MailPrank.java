package com.jackeri.school.mailprank;

import com.jackeri.school.mailprank.smtp.SmtpClient;

import java.io.*;
import java.util.*;

public class MailPrank {

    private Properties properties;
    private SmtpClient smtpClient;
    private SmtpClient smtpClientMailtrap;
    private VictimGroup[] groups;

    public MailPrank(String propertiesFileName, LinkedList<Victim> victims, LinkedList<Prank> pranks) {

        this.properties = new Properties();

        loadProperties(propertiesFileName);

        try {
            this.groups = new VictimGroup[Integer.parseInt(properties.getProperty("groups-count"))];
        } catch (NumberFormatException e) {
            System.out.printf("Couldn't use 'groups-count' property from %s!\n", propertiesFileName);
            System.exit(-1);
        }

        // Check arguments and properties
        if (pranks.size() < groups.length) {
            System.out.printf("Not enough pranks to create %d groups", groups.length);
            System.exit(-1);

        } else if (victims.size() / groups.length < 3) {
            System.out.printf("Not enough victims to have at least 3 per group!");
            System.exit(-1);
        }

        // Create smtp client
        smtpClient = new SmtpClient(
                properties.getProperty("smtp-host"),
                Integer.parseInt(properties.getProperty("smtp-port"))
        );
        smtpClientMailtrap = new SmtpClient(
                properties.getProperty("mailtrap-host"),
                Integer.parseInt(properties.getProperty("mailtrap-port"))
        );

        createGroups(victims, pranks);

        for (VictimGroup group : groups) {
            group.sendMails(smtpClient);
            group.sendMails(smtpClientMailtrap);
        }
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

    private void createGroups(LinkedList<Victim> victims, LinkedList<Prank> pranks) {

        Collections.shuffle(pranks);
        Iterator<Prank> itPranks = pranks.iterator();
        for (int i = 0; i < groups.length; ++i) {
            groups[i] = new VictimGroup(itPranks.next());
        }

        // Victims per group;
        final int victimsPerGroup = victims.size() / groups.length;

        // Create groups
        Collections.shuffle(victims);
        Iterator<Victim> it = victims.iterator();

        // Adds one sender and  'victimsPerGroup - 1' receivers to each group
        for (VictimGroup group : groups) {

            if (!it.hasNext()) {
                break;
            }
            group.setSender(it.next());

            for (int i = 1; i < victimsPerGroup && it.hasNext(); ++i) {
                group.addReceiver(it.next());
            }
        }

        // Add remaining victims as receivers.
        for (VictimGroup group : groups) {
            if (!it.hasNext()) {
                break;
            }
            group.addReceiver(it.next());
        }
    }

    public static void main(String[] args) {

        LinkedList<Victim> victims = new LinkedList<Victim>();

        // Create emails reader
        String emailsFileName = "src/main/resources/emails.txt";
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
            System.out.println(e.getMessage());
            System.out.println("Error found while reading emails!");
            return;
        }

        new MailPrank(
                "src/main/resources/config.properties", victims,
                new PrankParser("src/main/resources/pranks.txt", "---").getPranks()
        );
    }
}
