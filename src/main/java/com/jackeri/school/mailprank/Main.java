package com.jackeri.school.mailprank;

import com.jackeri.school.mailprank.smtp.SmtpClient;

import java.io.*;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {

//        System.out.println("Hello, World!");
//
//        LinkedList<Victim> victims = new LinkedList<Victim>();
//
//        try {
//            File emailsFile = new File("emails.txt");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(emailsFile), "UTF-8"));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                victims.add(new Victim(line));
//            }
//
////            final int nbGroups = victims.size() / 3;
////            for (int i = 0; i < nbGroups; ++i) {
////                for (int j = 0; j < 3; ++j) {
////
////                }
////            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return;
//        }

       SmtpClient smtpClient = new SmtpClient("localhost", 2525);

        Mail mail = new Mail(
                "nohan@jackeri.com",
                "steve@jackeri.com",
                "Prank",
                "Hello, this is a prank!"
        );

        try {
            if (smtpClient.sendMessage(mail)) {
                System.out.println("Mail sent!");
            } else {
                System.out.println("Mail not sent!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
