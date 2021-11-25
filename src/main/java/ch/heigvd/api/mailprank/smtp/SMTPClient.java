package ch.heigvd.api.mailprank.smtp;

import ch.heigvd.api.mailprank.mail.Mail;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class SMTPClient {

    private static final Logger LOG = Logger.getLogger(SMTPClient.class.getName());

    private final String serverAddress;
    private int serverPort = 25;

    public SMTPClient(String serverAddr, int serverPort){
        this.serverAddress = serverAddr;
        this.serverPort = serverPort;
    }

    void sendMail(Mail mail) throws IOException {
        LOG.info("Sending message with SMTP");
        Socket socketServer = new Socket(serverAddress, serverPort);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socketServer.getOutputStream(), StandardCharsets.UTF_8), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socketServer.getInputStream(), StandardCharsets.UTF_8));

        String line = reader.readLine();
        LOG.info(line);
        //must be the domain
        writer.println("EHLO localhost\r\n");

        line = reader.readLine();
        LOG.info(line);
        if(!line.startsWith("250")){
            throw new IOException("SMTP error: " + line);
        }

        while(line.startsWith("250")){
            line = reader.readLine();
            LOG.info(line);
        }

        sendSequence("MAIL FROM:", mail.getFrom(), writer, reader);

        for(String receiver : mail.getTo()){
            sendSequence("RCPT TO:", receiver, writer, reader);
        }

        writer.write("DATA\r\n");
        writer.flush();

        line = reader.readLine();
        LOG.info(line);

        writer.write("Content-Type: text/plain: charset=\"utf-8\"\r\n");
        writer.write("From: " + mail.getFrom() + "\r\n");

        writer.write("To: " + mail.getTo()[0]);
        for(int i = 1; i < mail.getTo().length; ++i){
            writer.write(", " + mail.getTo()[i]);
        }
        writer.write("\r\n");
        writer.flush();

        writer.write(mail.getBody());
        writer.write("\r\n");
        writer.write(".\r\n");
        writer.flush();

        line = reader.readLine();
        LOG.info(line);

        writer.write("QUIT\r\n");
        writer.flush();

        writer.close();
        reader.close();
        socketServer.close();

    }

    private void sendSequence(String toSend, String param, PrintWriter writer, BufferedReader reader) throws IOException {
        writer.write(toSend);
        writer.write(param);
        writer.write("\r\n");
        writer.flush();

        String line = reader.readLine();
        System.out.println(line);
    }
}
