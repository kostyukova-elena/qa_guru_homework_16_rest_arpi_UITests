package models;

@lombok.Data
public class AuthData {
    private String userName = System.getProperty("username");
    private String password = System.getProperty("password");
}
