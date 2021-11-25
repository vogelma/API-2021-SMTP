package ch.heigvd.api.mailprank;

import ch.heigvd.api.mailprank.configuration.ConfigurationManager;
import ch.heigvd.api.mailprank.mail.Content;
import ch.heigvd.api.mailprank.mail.Group;
import ch.heigvd.api.mailprank.mail.Person;
import ch.heigvd.api.mailprank.prank.PrankGenerator;

import java.util.List;

import static ch.heigvd.api.mailprank.configuration.ConfigurationManager.configureContents;
import static ch.heigvd.api.mailprank.configuration.ConfigurationManager.configureGroups;

public class main {
  public static void main(String[] args) {
    List<Group> groups = configureGroups(4);
    List<Content> content = configureContents();

  }

}
