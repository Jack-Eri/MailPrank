package com.jackeri.school.mailprank;

import com.jackeri.school.mailprank.smtp.SmtpClient;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the mail prank application.
 * Read properties, create groups and launches the SMTP connection.
 *
 * @author Nohan Budry, Andrés Moreno
 */
public class MailPrank {

    public static Logger LOG = Logger.getLogger(MailPrank.class.getSimpleName());

    private Properties properties;
    private SmtpClient smtpClient;
    private VictimGroup[] groups;

    /**
     * Starts the mail prank.
     * @param propertiesFileName file containing the properties needed by the program.
     */
    public MailPrank(String propertiesFileName) {

        this.properties = new Properties();

        loadProperties(propertiesFileName);

        try {
            this.groups = new VictimGroup[Integer.parseInt(properties.getProperty("groups-count"))];
        } catch (NumberFormatException e) {
            LOG.severe(String.format("Couldn't use 'groups-count' property from %s!\n", propertiesFileName));
            System.exit(-1);
        }

        // Parse pranks and victims
        LinkedList<Prank> pranks = new PranksParser(
                properties.getProperty("pranks-filename"),
                properties.getProperty("prank-separator")
        ).getPranks();

        LinkedList<Victim> victims = new VictimsParser(properties.getProperty("victims-filename")).getVictims();

        // Check properties
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
                properties.getProperty("smtp-password"),
                Integer.parseInt(properties.getProperty("smtp-cooldown"))
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

    /**
     * Application entry point
     *
     * @param args this apps doesn't use any arguments
     */
    public static void main(String[] args) {

        /*
         * Olivier Liechti prefers to have LOG output on a single line, it's easier to read. Being able
         * to change the formatting of console outputs is one of the reasons why it is
         * better to use a Logger rather than using System.out.println.
         *
         * We agree with him. (copied from Lab Java IO)
         */
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        new MailPrank("config.properties");
    }
}
