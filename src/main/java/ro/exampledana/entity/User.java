package ro.exampledana.entity;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Task> tasks;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.tasks= new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public List<Task> getTasks(){
        List<Task> copy= new ArrayList<>();
        copy.addAll(tasks);
        return copy;
    }

}
