package ch.heigvd.api.mailprank.prank;

import ch.heigvd.api.mailprank.mail.Mail;
import ch.heigvd.api.mailprank.smtp.SMTPClient;

import java.io.IOException;

public class Prank {
    private final Mail mail;
    private final String serverAddr;
    private final int serverPort;

    public Prank(Mail mail, String serverAddr, int serverPort) {
        this.mail = mail;
        this.serverAddr = serverAddr;
        this.serverPort = serverPort;
    }

    public void sendPrank() throws IOException {
        SMTPClient client = new SMTPClient(serverAddr, serverPort);
        client.sendMail(mail);
        System.out.println("Mail send");
    }


}
