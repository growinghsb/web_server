package model;

public class User {
    private String id;
    private String password;
    private String name;
    private String email;

    private User() {
    }

    private User(String id, String password, String name, String email) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }


    public String getPassword() {
        return password;
    }


    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }

    public static User createUser(String[] parameters) {
        return new User(parameters[0], parameters[1], parameters[2], parameters[3]);
    }
}
