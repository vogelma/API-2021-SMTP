package ch.heigvd.api.mailprank;

import ch.heigvd.api.mailprank.mail.Content;
import ch.heigvd.api.mailprank.mail.Group;
import ch.heigvd.api.mailprank.prank.PrankGenerator;

import java.util.List;

import static ch.heigvd.api.mailprank.configuration.ConfigurationManager.configureContents;
import static ch.heigvd.api.mailprank.configuration.ConfigurationManager.configureGroups;
import static java.lang.Integer.parseInt;

public class main {
  public static void main(String[] args) {
    if (args.length != 1) {
      throw new RuntimeException("Please provide the number of groups in argument.");
    }
    int nbGroups;
    try {
      nbGroups = parseInt(args[0]);
    } catch (Exception e) {
      throw new RuntimeException("The number of groups should be an integer.");
    }
    List<Group> groups = configureGroups(nbGroups);
    List<Content> content = configureContents();
    PrankGenerator.generate(groups, content);
  }
}
