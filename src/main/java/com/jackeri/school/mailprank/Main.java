package com.jackeri.school.mailprank;

import com.jackeri.school.mailprank.smtp.SmtpClient;

public class Main {

    public static void main(String[] args) {

        SmtpClient smtpClient = new SmtpClient("localhost", 2525);


        Victim sender = new Victim("patate@patate.ch");
        Victim[] victims = new Victim[]{
                new Victim("jack@patate.ch"),
                new Victim("steve@patate.ch"),
                new Victim("handy@patate.ch"),
        };

        VictimGroup group = new VictimGroup(sender, victims);
        group.sendMails(smtpClient);
    }
}
