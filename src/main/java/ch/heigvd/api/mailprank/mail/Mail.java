package ch.heigvd.api.mailprank.mail;

public class Mail {

    private final Content content;
    private final Person sender;
    private final Person[] receiver;

    public Mail(Content content, Person sender, Person[] receiver){
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Person getFrom(){
        return  sender;
    }

    public Person[] getTo(){
        return receiver;
    }

    public Content getBody(){
        return content;
    }
}
