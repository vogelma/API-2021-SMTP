package ch.heigvd.api.mailprank.mail;

public class Mail {

    private final String content;
    private final String sender;
    private final String[] receiver;

    public Mail(String content, String sender, String[] receiver){
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getFrom(){
        return  sender;
    }

    public String[] getTo(){
        return receiver;
    }

    public String getBody(){
        return content;
    }
}
