package com.jackeri.school.mailprank.smtp;


import java.io.*;
import java.net.Socket;


public class SmtpClient {


    private Socket socket;
    private PrintWriter pw;
    private BufferedReader br;
    private String response;


    public SmtpClient() {
        try {
            socket = new Socket("localhost", 2525);
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

            response = br.readLine();
            System.out.println(response);


            if (!response.contains("220")) {
                return;
            }


            pw.printf("EHLO mailprank\r\n");
            System.out.println("EHLO");

            for(int i=0;i<3;++i){
                System.out.println(br.readLine());
            }

            pw.printf("MAIL FROM: %s\r\n", "patat@lll.com");
            System.out.println("MAIL");

            System.out.println(br.readLine());

            pw.printf("RCPT TO: %s\r\n", "patat@lll.com");
            System.out.println("rcpt");

            System.out.println(br.readLine());

            pw.printf("DATA\r\n");
            System.out.println("DATA");

            System.out.println(br.readLine());

            pw.printf("From: qwer\r\n");
            pw.printf("To:qwertyu\r\n");
            pw.printf("Subject to: asfdsfs \r\n");
            pw.printf("\r\n");
            pw.printf("sdfghjklkjhgfd BODY fghjkjh\r\n");
            pw.printf(".\r\n");

            System.out.println(br.readLine());

            pw.printf("QUIT\r\n");
            System.out.println(br.readLine());
            socket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
