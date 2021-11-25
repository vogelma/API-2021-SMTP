package ch.heigvd.api.mailprank.mail;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final List<Person> members;

    public Group(List<Person> members){
        this.members = new ArrayList<>();
        for(Person p : members){
            this.members.add(p.clone());
        }
    }
}
