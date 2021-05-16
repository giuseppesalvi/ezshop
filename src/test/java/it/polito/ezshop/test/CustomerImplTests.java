package it.polito.ezshop.test;

import it.polito.ezshop.model.CustomerImpl;
import it.polito.ezshop.model.LoyaltyCard;

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
        LoyaltyCard card = new LoyaltyCard();
        CustomerImpl customer = new CustomerImpl("name",123,card);
        assertEquals(java.util.Optional.of(customer.getPoints()),java.util.Optional.of(0));
    }
    @Test
    public void testSetPoints() {
        LoyaltyCard card = new LoyaltyCard();
        CustomerImpl customer = new CustomerImpl("name",123,card);
        customer.setPoints(100);
        assertEquals(java.util.Optional.of(customer.getPoints()),java.util.Optional.of(100));
    }
    @Test
    public void testGetCard() {
        LoyaltyCard card = new LoyaltyCard("123");
        CustomerImpl customer = new CustomerImpl("name",123,card);
        assertEquals(customer.getCard(),card);
    }
    @Test
    public void testSetCard() {
        LoyaltyCard card = new LoyaltyCard("123");
        CustomerImpl customer = new CustomerImpl("name");
        customer.setCard(card);
        assertEquals(customer.getCard(),card);
    }
}
