package ch.heigvd.api.mailprank.smtp;

import ch.heigvd.api.mailprank.mail.Mail;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author Maëlle et Mélissa
 */
public class SMTPClient {

    private final String serverAddress;
    private final int serverPort;

    /**
     *
     * @param serverAddr smtp server address
     * @param serverPort smtp server port
     */
    public SMTPClient(String serverAddr, int serverPort){
        this.serverAddress = serverAddr;
        this.serverPort = serverPort;
    }

    /**
     *
     * @param mail mail which contains sender, receiver and body
     * @throws IOException writer and reader can throw some exception
     */
    public void sendMail(Mail mail) throws IOException {
        System.out.println("Sending message with SMTP");
        Socket socketServer = new Socket(serverAddress, serverPort);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socketServer.getOutputStream(), StandardCharsets.UTF_8), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socketServer.getInputStream(), StandardCharsets.UTF_8));

        String line = reader.readLine();
        System.out.println(line);
        //must be the domain
        writer.println("EHLO localhost\r\n");

        line = reader.readLine();
        System.out.println(line);

        //check error
        if(!line.startsWith("250")){
            throw new IOException("SMTP error: " + line);
        }


        while(line.startsWith("250")){
            line = reader.readLine();
            System.out.println(line);
        }

        sendSequence("MAIL FROM:", mail.getFrom(), writer, reader);

        for(String receiver : mail.getTo()){
            sendSequence("RCPT TO:", receiver, writer, reader);
        }

        writer.write("DATA\r\n");
        writer.flush();

        line = reader.readLine();
        System.out.println(line);

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
        System.out.println(line);

        writer.write("QUIT\r\n");
        writer.flush();

        writer.close();
        reader.close();
        socketServer.close();

    }

    /**
     *
     * @param toSend line to send
     * @param param parameter to add
     * @param writer writer to send
     * @param reader reader to print
     * @throws IOException writer and reader can throw some exception
     */
    private void sendSequence(String toSend, String param, PrintWriter writer, BufferedReader reader) throws IOException {
        writer.write(toSend);
        writer.write(param);
        writer.write("\r\n");
        writer.flush();

        String line = reader.readLine();
        System.out.println(line);
    }
}
