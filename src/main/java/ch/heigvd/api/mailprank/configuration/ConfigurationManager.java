package ch.heigvd.api.mailprank.configuration;

import ch.heigvd.api.mailprank.mail.Content;
import ch.heigvd.api.mailprank.mail.Group;
import ch.heigvd.api.mailprank.mail.Person;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.shuffle;

/**
 * Class used to get the messages and the email addresses from configuration files to set up the
 * victims and content for the prank
 *
 * @author Maëlle Vogel, Mélissa Gehring
 */
public class ConfigurationManager {
  // Make ConfigurationManager uninstanciable
  private ConfigurationManager() {}

  /**
   * Create nbGroups from the list of email addresses stored in victims.utf8
   *
   * @param nbGroups the number of groups to create
   * @return the list of Group created
   * @throws RuntimeException if the number of group doesn't divide the number of victims
   */
  public static List<Group> configureGroups(int nbGroups) {
    List<Person> victims = extractVictimsFromFile();
    if (victims.size() % nbGroups != 0) {
      throw new RuntimeException("Number of groups should divide the number of victims.");
    }

    // Randomize the order of the victims to form the groups randomly
    shuffle(victims);

    // Divide the victims into nbGroups sublists
    List<Group> groups = new ArrayList<>();
    int nbVictimsPerGroup = victims.size() / nbGroups;
    for (int i = 0; i < nbGroups; i++) {
      List<Person> groupVictims =
          victims.subList(i * nbVictimsPerGroup, (i + 1) * nbVictimsPerGroup);
      groups.add(new Group(groupVictims));
    }

    return groups;
  }

  /**
   * Get the list of Content from the content.utf8 configuration file
   *
   * @return the list of Content read from file
   * @throws RuntimeException in case of error while reading the file
   */
  public static List<Content> configureContents() {
    ArrayList<Content> contentList = new ArrayList<>();
    BufferedReader reader;

    try {
      reader =
          new BufferedReader(
              new InputStreamReader(
                  new FileInputStream("config/content.utf8"), StandardCharsets.UTF_8));
      Content currentReadContent;
      do {
        if ((currentReadContent = readSingleContent(reader)) != null) {
          contentList.add(currentReadContent);
        }
      } while (currentReadContent != null);
    } catch (Exception e) {
      throw new RuntimeException("Exception when reading content.utf8.");
    }
    return contentList;
  }

  /*
   * Reads the victims.utf8 configuration file to create a list
   * with all the potential victims and senders
   */
  private static List<Person> extractVictimsFromFile() {
    ArrayList<Person> victimsList = new ArrayList<>();
    BufferedReader reader;

    try {
      reader =
          new BufferedReader(
              new InputStreamReader(
                  new FileInputStream("config/victims.utf8"), StandardCharsets.UTF_8));
      String nextMail;
      while ((nextMail = reader.readLine()) != null) {
        // Each non-empty line is a new email address
        victimsList.add(new Person(nextMail));
      }
    } catch (Exception e) {
      throw new RuntimeException("Exception when reading victims.utf8.");
    }

    return victimsList;
  }

  /*
   * Read the next Content from the content.utf8 file
   */
  private static Content readSingleContent(BufferedReader reader) {
    String subject;
    StringBuilder body = new StringBuilder();
    String line;
    try {
      line = reader.readLine();
      if (line == null) { // We reached the end of the file
        return null;
      } else if (line.startsWith("Subject:")) {
        subject = line + "\n";
        String prefix = "";
        // The content ends with ** or with null if we reached the end of the file
        while ((line = reader.readLine()) != null && !line.contains("**")) {
          // Aggregate the body of the message
          body.append(prefix).append(line);
          prefix = "\n";
        }
      } else { // Content needs to start with Subject:
        throw new RuntimeException("Illegal content.utf8 format.");
      }
    } catch (Exception e) {
      throw new RuntimeException("Exception when reading content.utf8.");
    }
    return new Content(subject + body);
  }
}
