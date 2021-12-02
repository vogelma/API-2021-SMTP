package ch.heigvd.api.mailprank.prank;

import ch.heigvd.api.mailprank.mail.Mail;

import static ch.heigvd.api.mailprank.smtp.SMTPClient.sendMail;

/**
 * Class representing a prank played on a single group
 *
 * @author Maëlle Vogel, Mélissa Gehring
 */
public class Prank {
  private final Mail mail;

  /**
   * Constructor
   *
   * @param mail the email to send
   */
  public Prank(Mail mail) {
    this.mail = mail;
  }

  /** Send the prank email */
  public void sendPrank() {
    sendMail(mail);
    System.out.println("Mail sent");
  }
}
