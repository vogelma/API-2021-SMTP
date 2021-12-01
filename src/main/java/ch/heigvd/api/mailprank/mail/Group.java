package ch.heigvd.api.mailprank.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a group on which we launch a prank. The first Person in the list of members in
 * the group is the sender of the prank, the other are the receivers
 *
 * @author Maëlle Vogel, Mélissa Gehring
 */
public class Group {
  private final List<Person> members;

  /**
   * Constructor
   *
   * @param members the list of Person in the Group
   */
  public Group(List<Person> members) {
    this.members = new ArrayList<>();
    for (Person p : members) {
      this.members.add(p);
    }
  }

  /**
   * Get the sender of the prank, which is the first Person in the list of members
   *
   * @return the Person picked to be the sender
   */
  public Person getSender() {
    return members.get(0);
  }

  /**
   * Get the victims of the prank, which are the Persons in the list of member from index 1 to n - 1
   * where n is the number of members
   *
   * @return the list of victims
   */
  public List<Person> getVictims() {
    return members.subList(1, members.size());
  }
}
