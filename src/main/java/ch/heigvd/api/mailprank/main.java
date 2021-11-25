package ch.heigvd.api.mailprank;

import ch.heigvd.api.mailprank.configuration.ConfigurationManager;
import ch.heigvd.api.mailprank.mail.Group;
import ch.heigvd.api.mailprank.mail.Person;

import java.util.List;

import static ch.heigvd.api.mailprank.configuration.ConfigurationManager.configureGroups;

public class main {
  public static void main(String[] args) {
    List<Group> groups = configureGroups(4);
    for(Group g : groups){
      List<Person> members = g.getMembers();
      for(Person p : members){
        System.out.println(p.getMail());
      }
      System.out.println("*");
    }
  }

}
