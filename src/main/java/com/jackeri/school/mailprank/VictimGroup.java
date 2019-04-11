package com.jackeri.school.mailprank;

import com.jackeri.school.mailprank.smtp.ISmtpClient;

import java.io.IOException;
import java.util.LinkedList;

public class VictimGroup {

    private Victim sender;
    private LinkedList<Victim> receivers;
    private Prank prank;

    public VictimGroup(Prank prank) {

        this.sender = null;
        this.receivers = new LinkedList<Victim>();
        this.prank = prank;
    }

    public void sendMails(ISmtpClient smtpClient) {

        if (!isGroupReady()) {
            return;
        }

        for (Victim victim : receivers) {
            try {
                smtpClient.sendMessage(new Mail(sender.getMail(), victim.getMail(), prank.getSubject(), prank.getMessage()));
            } catch (IOException e) {
                System.out.printf("Couldn't send mail from %s to %s!\n", sender, victim);
                e.printStackTrace();
            }
        }
    }

    public void setSender(Victim sender) {
        this.sender = sender;
    }

    public void addReceiver(Victim victim) {
        if (victim != null)
            receivers.add(victim);{
        }
    }

    public boolean isGroupReady() {
        return sender != null && receivers.size() >= 2;
    }
}
