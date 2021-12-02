package ch.heigvd.api.mailprank;

import ch.heigvd.api.mailprank.mail.Content;
import ch.heigvd.api.mailprank.mail.Group;

import java.util.List;

import static ch.heigvd.api.mailprank.configuration.ConfigurationManager.configureContents;
import static ch.heigvd.api.mailprank.configuration.ConfigurationManager.configureGroups;
import static ch.heigvd.api.mailprank.prank.PrankGenerator.generatePranks;
import static java.lang.Integer.parseInt;

public class main {
  public static void main(String[] args) {
    // Argument check
    if (args.length != 1) {
      throw new RuntimeException("Please provide the number of groups in argument.");
    }
    int nbGroups;
    try {
      nbGroups = parseInt(args[0]);
    } catch (Exception e) {
      throw new RuntimeException("The number of groups should be an integer.");
    }

    // Fetch the groups from the file
    List<Group> groups = configureGroups(nbGroups);

    // Fetch the contents from the file
    List<Content> contents = configureContents();

    // Generate the pranks
    generatePranks(groups, contents);
  }
}
