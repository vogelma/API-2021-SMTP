package ch.heigvd.api.mailprank.smtp;

import ch.heigvd.api.mailprank.mail.Mail;
import ch.heigvd.api.mailprank.mail.Person;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
  private static final String CR_LF = "\r\n";

  // Make SMTPClient uninstanciable
  private SMTPClient() {}

  /**
   * Send the email address
   *
   * @param pranks list of emails which contains sender, receiver and body
   */
  public static void sendPranks(List<Mail> pranks) {
    System.out.println("Sending messages with SMTP");

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

      /* CONNEXION */
      String line = reader.readLine();
      System.out.println(line);
      // Must be the domain
      writer.write("EHLO localhost" + CR_LF);
      writer.flush();

      // Check error
      for (int i = 0; i < 3; i++) {
        line = reader.readLine();
        System.out.println(line);
        if (!line.startsWith("250")) {
          LOG.log(Level.SEVERE, "SMTP error: %s", line);
        }
      }

      /* SEND MAILS */
      for (Mail mail : pranks) {
        sendMail(mail, writer, reader);
      }

      /* QUIT */
      writer.write("QUIT" + CR_LF);
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
   * Send the mail
   */
  private static void sendMail(Mail mail, PrintWriter writer, BufferedReader reader)
      throws IOException {
    sendSequence("MAIL FROM: ", mail.getFrom().getMail(), writer, reader);

    for (Person receiver : mail.getTo()) {
      sendSequence("RCPT TO: ", receiver.getMail(), writer, reader);
    }

    sendData(mail, writer, reader);
  }

  /*
   * Send the sequence toSend + param
   */
  private static void sendSequence(
      String toSend, String param, PrintWriter writer, BufferedReader reader) throws IOException {
    writer.write(toSend + param + CR_LF);
    writer.flush();
    String line = reader.readLine();
    System.out.println(line);
  }

  /*
   * Send the DATA sequence of the mail
   */
  private static void sendData(Mail mail, PrintWriter writer, BufferedReader reader)
      throws IOException {
    writer.write("DATA" + CR_LF);
    writer.flush();

    String line = reader.readLine();
    System.out.println(line);

    StringBuilder sb = new StringBuilder();
    sb.append("Content-Type: text/plain: charset=\"utf-8\"")
        .append(CR_LF)
        .append("From: ")
        .append(mail.getFrom().getMail())
        .append(CR_LF)
        .append("To: ");
    String prefix = "";
    for (int i = 0; i < mail.getTo().size(); ++i) {
      sb.append(prefix).append(mail.getTo().get(i).getMail());
      prefix = ", ";
    }
    sb.append(CR_LF);
    writer.write(sb.toString());
    writer.flush();

    writer.write(mail.getBody().getContent() + CR_LF + "." + CR_LF);
    writer.flush();

    line = reader.readLine();
    System.out.println(line);
  }
}
