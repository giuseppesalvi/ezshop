package it.polito.ezshop.test;

import it.polito.ezshop.model.UserImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserImplTests {

    @Test
    public void testUserImpl() {
        // Test constructor with Id
        UserImpl user0 = new UserImpl(
                "user",
                "password",
                "Administrator",
                123);
        assertNotNull(user0);
        // Test constructor without Id
        UserImpl user1 = new UserImpl(
                "user",
                "password",
                "Administrator");
        assertNotNull(user1);
    }
    @Test
    public void testSettersGetters() {
        UserImpl user = new UserImpl(
                "user",
                "password",
                "Administrator",
                123);
        // setGetId
        user.setId(456);
        assertEquals(java.util.Optional.ofNullable(user.getId()),java.util.Optional.of(456));
        // setGetUsername
        user.setUsername("username");
        assertEquals(user.getUsername(),"username");
        // setGetPassword
        user.setPassword("pass");
        assertEquals(user.getPassword(),"pass");
        // setGetRole
        user.setRole("Administrator");
        assertEquals(user.getRole(),"Administrator");
        user.setRole("Cashier");
        assertEquals(user.getRole(),"Cashier");
        user.setRole("ShopManager");
        assertEquals(user.getRole(),"ShopManager");
    }
}