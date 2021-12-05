package ch.heigvd.api.mailprank.smtp;

import ch.heigvd.api.mailprank.mail.Mail;
import ch.heigvd.api.mailprank.mail.Person;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to send emails
 *
 * @author Maëlle Vogel, Mélissa Gehring
 */
public class SMTPClient {
  static final Logger LOG = Logger.getLogger(SMTPClient.class.getName());
  private static final String SERVER_ADDRESS = "localhost";
  private static final int SERVER_PORT = 2525;

  // Make SMTPClient uninstanciable
  private SMTPClient() {}

  /**
   * Send the email address
   *
   * @param mail email which contains sender, receiver and body
   */
  public static void sendMail(Mail mail) {
    System.out.println("Sending message with SMTP");

    Socket socketServer = null;
    PrintWriter writer = null;
    BufferedReader reader = null;

    try {
      socketServer = new Socket(SERVER_ADDRESS, SERVER_PORT);
      writer =
          new PrintWriter(
              new OutputStreamWriter(socketServer.getOutputStream(), StandardCharsets.UTF_8), true);
      reader =
          new BufferedReader(
              new InputStreamReader(socketServer.getInputStream(), StandardCharsets.UTF_8));

      String line = reader.readLine();
      System.out.println(line);
      // Must be the domain
      writer.write("EHLO localhost\r\n");
      writer.flush();

      // Check error
      for (int i = 0; i < 3; i++) {
        line = reader.readLine();
        System.out.println(line);
        if (!line.startsWith("250")) {
          LOG.log(Level.SEVERE, "SMTP error: %s", line);
        }
      }

      sendSequence("MAIL FROM: ", mail.getFrom().getMail(), writer, reader);

      for (Person receiver : mail.getTo()) {
        sendSequence("RCPT TO: ", receiver.getMail(), writer, reader);
      }

      writer.write("DATA\r\n");
      writer.flush();

      line = reader.readLine();
      System.out.println(line);

      writer.write("Content-Type: text/plain: charset=\"utf-8\"\r\n");
      writer.write("From: " + mail.getFrom().getMail() + "\r\n");

      writer.write("To: ");
      String prefix = "";
      for (int i = 0; i < mail.getTo().size(); ++i) {
        writer.write(prefix + mail.getTo().get(i).getMail());
        prefix = ", ";
      }
      writer.write("\r\n");
      writer.flush();

      writer.write(mail.getBody().getContent());
      writer.write("\r\n" + "." + "\r\n");
      writer.flush();

      line = reader.readLine();
      System.out.println(line);

      writer.write("QUIT\r\n");
      writer.flush();
    } catch (Exception e) {
      LOG.log(Level.SEVERE, e.getMessage(), e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException ex1) {
          LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
        }
      }

      if (writer != null) {
        writer.close();
      }

      if (socketServer != null) {
        try {
          socketServer.close();
        } catch (IOException ex1) {
          LOG.log(Level.SEVERE, ex1.getMessage(), ex1);
        }
      }
    }
  }

  /*
   * Send the sequence toSend + param
   */
  private static void sendSequence(
      String toSend, String param, PrintWriter writer, BufferedReader reader) {
    writer.write(toSend);
    writer.write(param);
    writer.write("\r\n");
    writer.flush();
    try {
      String line = reader.readLine();
      System.out.println(line);
    } catch (Exception e) {
      LOG.log(Level.SEVERE, e.getMessage(), e);
    }
  }
}
