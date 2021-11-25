package ch.heigvd.api.mailprank.smtp;

import ch.heigvd.api.mailprank.mail.Mail;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class SMTPClient implements ISMTPClient{

    private static final Logger LOG = Logger.getLogger(SMTPClient.class.getName());

    private String serverAddress;
    private int serverPort = 25;

    private PrintWriter writer;
    private BufferedReader reader;

    public SMTPClient(String serverAddr, int serverPort){
        this.serverAddress = serverAddr;
        this.serverPort = serverPort;
    }

    void sendMail(Mail mail) throws IOException {
        LOG.info("Sending message with SMTP");
        Socket socketServer = new Socket(serverAddress, serverPort);
        writer = new PrintWriter(new OutputStreamWriter(socketServer.getOutputStream(), "UTF-8"), true);
        reader = new BufferedReader(new InputStreamReader(socketServer.getInputStream(), "UTF-8"));

        String line = reader.readLine();
        LOG.info(line);
        writer.println("EHLO melma\r\n");

        line = reader.readLine();
        LOG.info(line);
        if(!line.startsWith("250")){
            throw new IOException("SMTP error: " + line);
        }

        while(line.startsWith("250")){
            line = reader.readLine();
            LOG.info(line);
        }

        writer.write("MAIL FROM:");
        writer.write(mail.getFrom()); //to implement
        writer.write("\r\n");
        writer.flush();

        line = reader.readLine();
        LOG.info(line);

        for(String receiver : mail.getTo()){
            writer.write("RCPT TO:");
            writer.write(receiver);
            writer.write("\r\n");
            writer.flush();

            line = reader.readLine();
            LOG.info(line);
        }

        for(String receiver : mail.getCc()){
            writer.write("RCPT TO:");
            writer.write(receiver);
            writer.write("\r\n");
            writer.flush();

            line = reader.readLine();
            LOG.info(line);
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

        LOG.info(mail.getBody());
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

    //mettre write en pointeur?
    private String sendSequence(String toSend){
        return "";
    }
}
