package ch.heigvd.api.mailprank.mail;

/**
 * Class representing a Person in the context of the prank
 *
 * @author Maëlle Vogel, Mélissa Gehring
 */
public class Person {
  private final String mail;

  /**
   * Constructor
   *
   * @param mail the email address of the Person
   */
  public Person(String mail) {
    this.mail = mail;
  }

  /**
   * Getter
   *
   * @return the email address of the Person
   */
  public String getMail() {
    return mail;
  }
}
