package com.jackeri.school.mailprank;

public class VictimGroup {

    private Victim sender;
    private Victim[] receivers;

    public VictimGroup(Victim sender, Victim[] victims) {

        this.sender = sender;
        this.receivers = victims;
    }

    public void sendMails() {

        System.out.println("Sender: " + sender);
        System.out.println("Recivers:");
        for (Victim victim : receivers) {
            System.out.println(victim);
        }
    }

    public Victim getSender() {
        return sender;
    }

    public Victim[] getReceivers() {
        return receivers;
    }
}
