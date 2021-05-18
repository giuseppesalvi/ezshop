package it.polito.ezshop.test;

import it.polito.ezshop.model.UserImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserImplTests {

    @Test
    public void testUserImpl() {
        UserImpl user0 = new UserImpl("user","password","Admin", 123);
        UserImpl user1 = new UserImpl("user","password","Admin");
        assertNotNull(user0);
        assertNotNull(user1);
    }
    @Test
    public void testUserImplWithNullID() {
        UserImpl user = new UserImpl("user","password","Admin");
        assertNotNull(user.getId());
    }
    @Test
    public void testGetId() {
        UserImpl user = new UserImpl("user","password","Admin", 123);
        assertEquals(java.util.Optional.ofNullable(user.getId()),java.util.Optional.ofNullable(123));
    }
    @Test
    public void testSetIdWithValidId () {
        UserImpl user = new UserImpl("user","password","Admin", 123);
        user.setId(456);
        assertEquals(java.util.Optional.ofNullable(user.getId()),java.util.Optional.of(456));
    }
    @Test
    public void testGetUsername() {
        UserImpl user = new UserImpl("user","password","Admin");
        assertEquals(user.getUsername(),"user");
    }
    @Test
    public void testSetUsername() {
        // TODO test with invalid inputs
        UserImpl user = new UserImpl("user","password","Admin", 123);
        user.setUsername("username");
        assertEquals(user.getUsername(),"username");
    }
    @Test
    public void testGetPassword() {
        UserImpl user = new UserImpl("user","password","Admin");
        assertEquals(user.getPassword(),"password");
    }
    @Test
    public void testSetPassword() {
        // TODO test with invalid inputs
        UserImpl user = new UserImpl("user","password","Admin", 123);
        user.setPassword("pass");
        assertEquals(user.getPassword(),"pass");
    }
    @Test
    public void testGetRole() {
        UserImpl user = new UserImpl("user","password","Admin");
        assertEquals(user.getRole(),"Admin");
    }
    @Test
    public void testSetRole(){
        // TODO test with invalid inputs
        UserImpl user = new UserImpl("user","password","Admin", 123);
        user.setRole("Administrator");
        assertEquals(user.getRole(),"Administrator");
        user.setRole("Cashier");
        assertEquals(user.getRole(),"Cashier");
        user.setRole("ShopManager");
        assertEquals(user.getRole(),"ShopManager");
    }
}