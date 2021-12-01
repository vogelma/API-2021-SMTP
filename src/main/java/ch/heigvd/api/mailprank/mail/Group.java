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

    public Person getSender(){
        return members.get(0);
    }

    public Person[] getVictims(){
        Person[] victimsMail = new Person[members.size()-1];
        for(int i = 1; i < members.size(); i++){
            victimsMail[i-1] = members.get(i);
        }
        return victimsMail;
    }
}
