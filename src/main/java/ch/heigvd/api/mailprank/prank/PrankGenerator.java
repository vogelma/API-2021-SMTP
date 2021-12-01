package ch.heigvd.api.mailprank.prank;

import ch.heigvd.api.mailprank.mail.Content;
import ch.heigvd.api.mailprank.mail.Group;
import ch.heigvd.api.mailprank.mail.Mail;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Class used to generate a prank for each of the groups of Person
 *
 * @author Maëlle Vogel, Mélissa Gehring
 */
public class PrankGenerator {
  private static final String SERVER_ADDRESS = "localhost";
  private static final int SERVER_PORT = 25;

  // Make PrankGenerator uninstanciable
  private PrankGenerator() {}

  /**
   * Generate a prank for each group, picking each time one content randomly from the list
   *
   * @param groups the list of groups on which we have to play the pranks
   * @param content the list of Content we can pick the message from
   * @throws IOException if a prank couldn't be sent
   */
  public static void generate(List<Group> groups, List<Content> content) throws IOException {
    Random rng = new Random();
    for (Group group : groups) {
      int nextContentIdx = rng.nextInt(content.size());
      Mail mail = new Mail(content.get(nextContentIdx), group.getSender(), group.getVictims());
      Prank prank = new Prank(mail, SERVER_ADDRESS, SERVER_PORT);
      prank.sendPrank();
    }
  }
}
