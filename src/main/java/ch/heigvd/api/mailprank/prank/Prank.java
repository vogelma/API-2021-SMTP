package ch.heigvd.api.mailprank.prank;

import ch.heigvd.api.mailprank.mail.Group;
import ch.heigvd.api.mailprank.mail.Person;

public class Prank {
    private final Group victims;
    private Person sender;

    public Prank(Group victims) {
        this.victims = victims;
    }


}
