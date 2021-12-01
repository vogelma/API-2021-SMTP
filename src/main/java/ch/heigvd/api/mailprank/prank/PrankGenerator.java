package ch.heigvd.api.mailprank.prank;

import ch.heigvd.api.mailprank.mail.Content;
import ch.heigvd.api.mailprank.mail.Group;
import ch.heigvd.api.mailprank.mail.Mail;

import java.io.IOException;
import java.util.List;

public class PrankGenerator {
     public static void generate(List<Group> group, List<Content> content) throws IOException {
        Mail mail = new Mail(content.get(0), group.get(0).getSender(), group.get(0).getVictims());
        Prank prank = new Prank( mail, "smtp.heig-vd.ch", 25);
        prank.sendPrank();
    }
}
