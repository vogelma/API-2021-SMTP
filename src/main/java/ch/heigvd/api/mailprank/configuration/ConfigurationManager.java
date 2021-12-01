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

public class ConfigurationManager {

  private ConfigurationManager() {}

  public static List<Group> configureGroups(int nbGroups) {
    List<Person> victims = extractVictimsFromFile();
    if (victims.size() % nbGroups != 0) {
      throw new RuntimeException("Number of groups should divide the number of victims.");
    }

    shuffle(victims); // randomize the order of the victims to form the groups randomly

    List<Group> groups = new ArrayList<>();
    int nbVictimsPerGroup = victims.size() / nbGroups;
    for (int i = 0; i < nbGroups; i++) {
      List<Person> groupVictims =
          victims.subList(i * nbVictimsPerGroup, (i + 1) * nbVictimsPerGroup);
      groups.add(new Group(groupVictims));
    }

    return groups;
  }

  private static List<Person> extractVictimsFromFile() {
    ArrayList<Person> victimsList = new ArrayList<>();

    BufferedReader reader;

    try {
      reader =
          new BufferedReader(
              new InputStreamReader(
                  new FileInputStream("config/victims.utf8"), StandardCharsets.UTF_8));
      String nextMail = "";
      while ((nextMail = reader.readLine()) != null) {
        victimsList.add(new Person(nextMail));
      }
    } catch (Exception e) {
      throw new RuntimeException("Exception when reading victims.utf8.");
    }

    return victimsList;
  }

  public static List<Content> configureContents() {
    ArrayList<Content> contentList = new ArrayList<>();

    BufferedReader reader;

    try {
      reader =
          new BufferedReader(
              new InputStreamReader(
                  new FileInputStream("config/content.utf8"), StandardCharsets.UTF_8));
      Content currentReadContent = null;
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

  private static Content readSingleContent(BufferedReader reader) {
    String subject = "";
    StringBuilder body = new StringBuilder();
    String line;
    try {
      line = reader.readLine();
      if (line == null) {
        return null;
      } else if (line.startsWith("Subject:")) {
        subject = line + "\n";
        String prefix = "";
        while ((line = reader.readLine()) != null && !line.contains("**")) {
          body.append(prefix).append(line);
          prefix = "\n";
        }
      } else {
        throw new RuntimeException("Illegal content.utf8 format.");
      }
    } catch (Exception e) {
      throw new RuntimeException("Exception when reading content.utf8.");
    }
    return new Content(subject + body);
  }
}
