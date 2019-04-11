package com.jackeri.school.mailprank;

import com.jackeri.school.mailprank.smtp.ISmtpClient;

import java.io.IOException;
import java.util.LinkedList;

public class VictimGroup {

    private Victim sender;
    private LinkedList<Victim> receivers;

    public VictimGroup(Victim sender) {

        this.sender = sender;
        this.receivers = new LinkedList<Victim>();
    }

    public void sendMails(ISmtpClient smtpClient) {

        for (Victim victim : receivers) {
            try {
                smtpClient.sendMessage(new Mail(sender.getMail(), victim.getMail(), "Test", "Test Test"));
            } catch (IOException e) {
                System.out.printf("Couldn't send mail from %s to %s!\n", sender, victim);
                e.printStackTrace();
            }
        }
    }

    public void addReceiver(Victim victim) {
        if (victim != null)
            receivers.add(victim);{
        }
    }

    public boolean isGroupReady() {
        return receivers.size() >= 3;
    }
}
