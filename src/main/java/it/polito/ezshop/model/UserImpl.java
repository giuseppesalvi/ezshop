package it.polito.ezshop.model;

import it.polito.ezshop.data.User;

public class UserImpl implements User {

    static Integer idGen = 0;
    String username;
    String password;
    String role;
    Integer id;

    public UserImpl(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.id = idGen++;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }
}
