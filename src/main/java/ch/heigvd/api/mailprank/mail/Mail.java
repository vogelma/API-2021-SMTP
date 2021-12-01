package ch.heigvd.api.mailprank.mail;

import java.util.List;

/**
 * Class representing an email
 *
 * @author Maëlle Vogel, Mélissa Gehring
 */
public class Mail {
  private final Content content;
  private final Person sender;
  private final List<Person> receiver;

  /**
   * Constructor
   *
   * @param content the message to send
   * @param sender the sender of the mail
   * @param receiver the list of person who will receive the mail
   */
  public Mail(Content content, Person sender, List<Person> receiver) {
    this.content = content;
    this.sender = sender;
    this.receiver = receiver;
  }

  /**
   * Getter
   *
   * @return the sender of the email
   */
  public Person getFrom() {
    return sender;
  }

  /**
   * Getter
   *
   * @return the list of Person receiving the email
   */
  public List<Person> getTo() {
    return receiver;
  }

  /**
   * Getter
   *
   * @return the content of the email
   */
  public Content getBody() {
    return content;
  }
}
