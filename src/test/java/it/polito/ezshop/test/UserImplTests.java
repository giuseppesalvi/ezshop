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
    public void testGetUsername() {
        UserImpl user = new UserImpl("user","password","Admin");
        assertEquals(user.getUsername(),"user");
    }

    @Test
    public void testGetPassword() {
        UserImpl user = new UserImpl("user","password","Admin");
        assertEquals(user.getPassword(),"password");
    }

    @Test
    public void testGetRole() {
        UserImpl user = new UserImpl("user","password","Admin");
        assertEquals(user.getRole(),"Admin");
    }

}