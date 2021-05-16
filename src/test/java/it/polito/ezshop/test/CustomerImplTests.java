package it.polito.ezshop.test;
import it.polito.ezshop.model.CustomerImpl;

import org.junit.Test;
import static org.junit.Assert.*;

public class CustomerImplTests {

    @Test
    public void testGetCustomerName() {
        CustomerImpl customer = new CustomerImpl("name");
        assertEquals(customer.getCustomerName(), "name");
    }

    @Test
    public void testSetCustomerName() {
        CustomerImpl customer = new CustomerImpl(1);
        customer.setCustomerName("name");
        assertEquals(customer.getCustomerName(), "name");
    }

    @Test
    public void testGetId() {
        CustomerImpl customer = new CustomerImpl(1 );
        assertEquals(java.util.Optional.of(customer.getId()), java.util.Optional.of(1));
    }
    @Test
    public void testSetId() {
        CustomerImpl customer = new CustomerImpl("name" );
        customer.setId(2);
        assertEquals(java.util.Optional.of(customer.getId()) , java.util.Optional.of(2));
    }
    @Test
    public void testGetPoints() {
    }
    @Test
    public void testSetPoints() {
    }
    @Test
    public void testGetCard() {
    }
    @Test
    public void testSetCard() {
    }
}
