package ch.heigvd.api.mailprank.smtp;

import ch.heigvd.api.mailprank.mail.Mail;
import ch.heigvd.api.mailprank.mail.Person;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Class used to send emails
 *
 * @author Maëlle Vogel, Mélissa Gehring
 */
public class SMTPClient {
  private final String serverAddress;
  private final int serverPort;

  /**
   * Constructor
   *
   * @param serverAddr SMTP server address
   * @param serverPort SMTP server port
   */
  public SMTPClient(String serverAddr, int serverPort) {
    this.serverAddress = serverAddr;
    this.serverPort = serverPort;
  }

  /**
   * Send the email address
   *
   * @param mail email which contains sender, receiver and body
   * @throws IOException if writer and reader fail to send
   */
  public void sendMail(Mail mail) throws IOException {
    System.out.println("Sending message with SMTP");
    Socket socketServer = new Socket(serverAddress, serverPort);
    PrintWriter writer =
        new PrintWriter(
            new OutputStreamWriter(socketServer.getOutputStream(), StandardCharsets.UTF_8), true);
    BufferedReader reader =
        new BufferedReader(
            new InputStreamReader(socketServer.getInputStream(), StandardCharsets.UTF_8));

    String line = reader.readLine();
    System.out.println(line);
    // Must be the domain
    writer.println("EHLO localhost\r\n");

    line = reader.readLine();
    System.out.println(line);

    // Check error
    if (!line.startsWith("250")) {
      throw new IOException("SMTP error: " + line);
    }

    while (line.startsWith("250")) {
      line = reader.readLine();
      System.out.println(line);
    }

    sendSequence("MAIL FROM:", mail.getFrom().getMail(), writer, reader);

    for (Person receiver : mail.getTo()) {
      sendSequence("RCPT TO:", receiver.getMail(), writer, reader);
    }

    writer.write("DATA\r\n");
    writer.flush();

    line = reader.readLine();
    System.out.println(line);

    writer.write("Content-Type: text/plain: charset=\"utf-8\"\r\n");
    writer.write("From: " + mail.getFrom() + "\r\n");

    writer.write("To: " + mail.getTo().get(0));
    for (int i = 1; i < mail.getTo().size(); ++i) {
      writer.write(", " + mail.getTo().get(i));
    }
    writer.write("\r\n");
    writer.flush();

    writer.write(mail.getBody().getContent());
    writer.write("\r\n");
    writer.write(".\r\n");
    writer.flush();

    line = reader.readLine();
    System.out.println(line);

    writer.write("QUIT\r\n");
    writer.flush();

    writer.close();
    reader.close();
    socketServer.close();
  }

  /*
   * Send the sequence toSend + param
   */
  private void sendSequence(String toSend, String param, PrintWriter writer, BufferedReader reader)
      throws IOException {
    writer.write(toSend);
    writer.write(param);
    writer.write("\r\n");
    writer.flush();

    String line = reader.readLine();
    System.out.println(line);
  }
}
