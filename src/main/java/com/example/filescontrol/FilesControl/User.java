package com.example.filescontrol.FilesControl;

public class User {
    String name;
    String password;
    int id;
    public User(String name, String password)
    {
        this.name = name;
        this.password = password;
    }
    public User(String name, String password, int id)
    {
        this.name = name;
        this.password = password;
        this.id = id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
