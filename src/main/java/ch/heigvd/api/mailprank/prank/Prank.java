package ch.heigvd.api.mailprank.prank;

import ch.heigvd.api.mailprank.mail.Group;
import ch.heigvd.api.mailprank.mail.Person;
import ch.heigvd.api.mailprank.smtp.SMTPClient;

public class Prank {
    private final Group victims;
    private Person sender;
    private SMTPClient client;

    public Prank(Group victims) {
        this.victims = victims;
    }


}
