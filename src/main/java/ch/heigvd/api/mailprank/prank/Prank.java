package ch.heigvd.api.mailprank.prank;

import ch.heigvd.api.mailprank.mail.Mail;
import ch.heigvd.api.mailprank.smtp.SMTPClient;

import java.io.IOException;

/**
 * Class representing a prank played on a single group
 *
 * @author Maëlle Vogel, Mélissa Gehring
 */
public class Prank {
  private final Mail mail;
  private final String serverAddr;
  private final int serverPort;

  /**
   * Constructor
   *
   * @param mail the email to send
   * @param serverAddr the server address where to send the email
   * @param serverPort the server port to use to send the email
   */
  public Prank(Mail mail, String serverAddr, int serverPort) {
    this.mail = mail;
    this.serverAddr = serverAddr;
    this.serverPort = serverPort;
  }

  /**
   * Send the prank email
   *
   * @throws IOException if the email couldn't be sent
   */
  public void sendPrank() throws IOException {
    SMTPClient client = new SMTPClient(serverAddr, serverPort);
    client.sendMail(mail);
    System.out.println("Mail send");
  }
}
