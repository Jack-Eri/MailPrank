package com.jackeri.school.mailprank;

import com.jackeri.school.mailprank.smtp.SmtpClient;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MailPrank {

    public static Logger LOG = Logger.getLogger(MailPrank.class.getSimpleName());
;
    private Properties properties;
    private SmtpClient smtpClient;
    private VictimGroup[] groups;

    public MailPrank(String propertiesFileName, LinkedList<Victim> victims, LinkedList<Prank> pranks) {

        this.properties = new Properties();

        loadProperties(propertiesFileName);

        try {
            this.groups = new VictimGroup[Integer.parseInt(properties.getProperty("groups-count"))];
        } catch (NumberFormatException e) {
            LOG.severe(String.format("Couldn't use 'groups-count' property from %s!\n", propertiesFileName));
            System.exit(-1);
        }

        // Check arguments and properties
        if (pranks.size() < groups.length) {
            LOG.severe(String.format("Not enough pranks to create %d groups", groups.length));
            System.exit(-1);

        } else if (victims.size() / groups.length < VictimGroup.MIN_GROUP_SIZE) {
            LOG.severe(String.format("Not enough victims to have at least %d per group!", VictimGroup.MIN_GROUP_SIZE));
            System.exit(-1);
        }

        // Create smtp client
        smtpClient = new SmtpClient(
                properties.getProperty("smtp-host"),
                Integer.parseInt(properties.getProperty("smtp-port")),
                properties.getProperty("smtp-username"),
                properties.getProperty("smtp-password")
        );

        createGroups(victims, pranks);

        for (VictimGroup group : groups) {
            group.sendMails(smtpClient);
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
            LOG.severe(String.format("Failed loading properties from %s!", filename));
            System.exit(-1);
        }
    }

    /**
     * Create groups with given pranks and spread victims to them.
     * Victims and pranks are shuffled. All victims are used while 'groups count' pranks are used.
     * @param victims list of victims
     * @param pranks list of pranks
     */
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

        /*
         * Olivier Liechti prefers to have LOG output on a single line, it's easier to read. Being able
         * to change the formatting of console outputs is one of the reasons why it is
         * better to use a Logger rather than using System.out.println.
         *
         * We agree with him. (copied from Lab Java IO)
         */
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        LOG.setLevel(Level.INFO);

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
            System.out.println(e.getMessage());
            System.out.println("Error found while reading emails!");
            return;
        }

        new MailPrank(
                "config.properties", victims,
                new PrankParser("pranks.txt", "---").getPranks()
        );
    }
}
