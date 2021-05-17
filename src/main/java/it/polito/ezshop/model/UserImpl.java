package it.polito.ezshop.model;

import it.polito.ezshop.data.EZShopMaps;
import it.polito.ezshop.data.User;

public class UserImpl implements User {

    static public Integer idGen = 1;
    private String username;
    private String password;
    private String role;
    private Integer id;

    //Constructor for new users
    public UserImpl(String username, String password, String role){
        this.username = username;
        this.password = password;
        this.role = role;
        this.id = idGen++;
    }

    //Constructor for User with already an ID
    public UserImpl(String username, String password, String role, Integer id) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.id = id;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        if (id != null && !EZShopMaps.users.containsKey(id)) {
            this.id = id;
            if (id > idGen)
                idGen = id+1;
        }
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void setUsername(String username) {
        if (username != null &&
                !username.isEmpty() &&
                EZShopMaps.users.values().stream().noneMatch(u -> u.getUsername().contentEquals(username))) {
            this.username = username;
        }
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        if (password != null && !password.isEmpty()) {
            this.password = password;
        }
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        if (role != null &&
                (role.contentEquals("Administrator") ||
                        role.contentEquals("Cashier") ||
                        role.contentEquals("ShopManager"))) {
            this.role = role;
        }
    }

}
