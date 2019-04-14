package com.jackeri.school.mailprank;

import com.jackeri.school.mailprank.smtp.ISmtpClient;
import com.jackeri.school.mailprank.smtp.Mail;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Represents a group of victims for a prank.
 * Contains the victims (one sender and at least two receivers) and the prank.
 *
 * @author Nohan Budry, Andrés Moreno
 */
public class VictimGroup {

    public static final int MIN_GROUP_SIZE = 3;

    private Victim sender;
    private LinkedList<Victim> receivers;
    private Prank prank;

    /**
     * Create a new group with it's pranks.
     * The sender and the receivers should be added with setSender() and addReceiver() methods.
     *
     * @param prank the prank
     */
    public VictimGroup(Prank prank) {

        this.sender = null;
        this.receivers = new LinkedList<>();
        this.prank = prank;
    }

    /**
     * Sends the prank from the sender to each receivers.
     * Nothing hapens if the groups isn't ready (requires one sender and at least two receivers).
     * @param smtpClient the smtp client used to send the mails
     */
    public void sendMails(ISmtpClient smtpClient) {

        if (!isGroupReady()) {
            return;
        }

        int index = 0;
        String[] mailAdresses = new String[receivers.size()];
        for (Victim victim : receivers) {
            mailAdresses[index] = victim.getMail();
            index++;
        }

        try {
            smtpClient.sendMessage(new Mail(sender.getMail(), mailAdresses, prank.getSubject(), prank.getMessage()));
        } catch (IOException e) {
            MailPrank.LOG.severe(String.format("Couldn't send mails: %s", e.getMessage()));
        }
    }

    /**
     * Sets which victim represents the sender of the prank.
     * @param sender the victim used as the sender
     */
    public void setSender(Victim sender) {
        this.sender = sender;
    }

    /**
     * Adds a victim as a receiver of the prank.
     * @param victim the victim used as a receiver
     */
    public void addReceiver(Victim victim) {
        if (victim != null)
            receivers.add(victim);{
        }
    }

    /**
     * Checks if the group is ready to send the emails.
     * Requires one sender and at least two victims.
     *
     * @return true if the group is ready.
     */
    public boolean isGroupReady() {
        return sender != null && receivers.size() >= MIN_GROUP_SIZE - 1;
    }
}
