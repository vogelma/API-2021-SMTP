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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.shuffle;
import static java.util.regex.Pattern.compile;

/**
 * Class used to get the messages and the email addresses from configuration files to set up the
 * victims and content for the prank
 *
 * @author Maëlle Vogel, Mélissa Gehring
 */
public class ConfigurationManager {
  static final Logger LOG = Logger.getLogger(ConfigurationManager.class.getName());

  private static final String CONTENT_FILE_PATH = "config/content.utf8";
  private static final String VICTIMS_FILE_PATH = "config/victims.utf8";

  // Make ConfigurationManager uninstanciable
  private ConfigurationManager() {}

  /**
   * Create nbGroups from the list of email addresses stored in victims.utf8
   *
   * @param nbGroups the number of groups to create
   * @return the list of Group created
   * @throws RuntimeException if the number of group doesn't divide the number of victims or if the
   *     number of person in a group is smaller than 3
   */
  public static List<Group> configureGroups(int nbGroups) {
    // Get all victims from the file
    List<Person> victims = extractVictimsFromFile();

    // Check that the number of groups fit with the number of victims
    if (victims.size() % nbGroups != 0) {
      throw new RuntimeException("Number of groups should divide the number of victims.");
    }
    int nbVictimsPerGroup = victims.size() / nbGroups;
    if (nbVictimsPerGroup < 3) {
      throw new RuntimeException("There should be at least 3 persons per group.");
    }

    // Randomize the order of the victims to form the groups randomly
    shuffle(victims);

    // Divide the victims into nbGroups sublists
    List<Group> groups = new ArrayList<>();
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
   */
  public static List<Content> configureContents() {
    ArrayList<Content> contentList = new ArrayList<>();
    BufferedReader reader;

    try {
      reader =
          new BufferedReader(
              new InputStreamReader(
                  new FileInputStream(CONTENT_FILE_PATH), StandardCharsets.UTF_8));
      Content currentReadContent;
      do { // Read and store the next mail content
        if ((currentReadContent = readSingleContent(reader)) != null) {
          contentList.add(currentReadContent);
        }
      } while (currentReadContent != null);
    } catch (Exception e) {
      LOG.log(Level.SEVERE, e.getMessage(), e);
    }
    return contentList;
  }

  /*
   * Read the victims.utf8 configuration file to create a list
   * with all the potential victims and senders
   */
  private static List<Person> extractVictimsFromFile() {
    ArrayList<Person> victimsList = new ArrayList<>();
    BufferedReader reader;

    try {
      reader =
          new BufferedReader(
              new InputStreamReader(
                  new FileInputStream(VICTIMS_FILE_PATH), StandardCharsets.UTF_8));
      String nextMail;
      while ((nextMail = reader.readLine()) != null) {
        // Each non-empty line is a new email address
        checkMailFormat(nextMail);
        victimsList.add(new Person(nextMail));
      }
    } catch (Exception e) {
      LOG.log(Level.SEVERE, e.getMessage(), e);
    }

    return victimsList;
  }

  /*
   * Read the next Content from the content.utf8 file
   */
  private static Content readSingleContent(BufferedReader reader) {
    String subject = null;
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
      LOG.log(Level.SEVERE, e.getMessage(), e);
    }

    return new Content(subject + body);
  }

  /*
   * Check the format of the mail address
   */
   private static void checkMailFormat(String mail) {
     Pattern pattern = compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
     Matcher mat = pattern.matcher(mail);
     if(!mat.matches()){
       throw new RuntimeException("Invalid email format");
     }
   }
}
