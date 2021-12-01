package ch.heigvd.api.mailprank.mail;

/**
 * Class representing the content of an email, the message we're sending
 *
 * @author Maëlle Vogel, Mélissa Gehring
 */
public class Content {
  private final String content;

  /**
   * Constructor
   *
   * @param content the content of the email
   */
  public Content(String content) {
    this.content = content;
  }

  /**
   * Getter
   *
   * @return the content
   */
  public String getContent() {
    return content;
  }
}
