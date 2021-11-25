package ch.heigvd.api.mailprank.configuration;

import ch.heigvd.api.mailprank.mail.Content;
import ch.heigvd.api.mailprank.mail.Group;
import ch.heigvd.api.mailprank.mail.Person;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationManager implements IConfigurationManager {
    private List<Person> victims;
    private List<Content> content;

    public ConfigurationManager(){
        victims = new ArrayList<>();
        content = new ArrayList<>();
    }

    public Group fetchGroupFromFile(){

    }
}
