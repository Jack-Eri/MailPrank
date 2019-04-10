package com.jackeri.school.mailprank;

import com.jackeri.school.mailprank.smtp.SmtpClient;

import java.io.IOException;

public class VictimGroup {

    private Victim sender;
    private Victim[] receivers;

    public VictimGroup(Victim sender, Victim[] victims) {

        this.sender = sender;
        this.receivers = victims;
    }

    public void sendMails(SmtpClient smtpClient) {

        for (Victim victim : receivers) {
            try {
                smtpClient.sendMessage(new Mail(sender.getMail(), victim.getMail(), "Test", "Test Test"));
            } catch (IOException e) {
                System.out.printf("Couldn't send mail from %s to %s!\n", sender, victim);
                e.printStackTrace();
            }
        }
    }

    public Victim getSender() {
        return sender;
    }

    public Victim[] getReceivers() {
        return receivers;
    }
}
