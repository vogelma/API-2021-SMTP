package ch.heigvd.api.mailprank.mail;

public class Person {
    private final String mail;

    public Person(String mail){
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    @Override
    public Person clone(){
        return new Person(mail);
    }
}
