package ch.heigvd.api.mailprank.prank;

import ch.heigvd.api.mailprank.mail.Content;
import ch.heigvd.api.mailprank.mail.Group;
import ch.heigvd.api.mailprank.mail.Mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static ch.heigvd.api.mailprank.smtp.SMTPClient.sendPranks;

/**
 * Class used to generate a prank for each of the groups of Person
 *
 * @author Maëlle Vogel, Mélissa Gehring
 */
public class PrankGenerator {
  // Make PrankGenerator uninstanciable
  private PrankGenerator() {}

  /**
   * Generate a prank (mail) for each group, picking each time one content randomly from the list
   *
   * @param groups the list of groups on which we have to play the pranks
   * @param content the list of Content we can pick the message from
   */
  public static void generatePranks(List<Group> groups, List<Content> content) {
    Random rng = new Random();
    List<Mail> pranks = new ArrayList<>();
    for (Group group : groups) {
      int nextContentIdx = rng.nextInt(content.size());
      pranks.add(new Mail(content.get(nextContentIdx), group.getSender(), group.getVictims()));
    }

    sendPranks(pranks);
  }
}
