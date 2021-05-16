package it.polito.ezshop.test;

import it.polito.ezshop.model.CustomerImpl;
import it.polito.ezshop.model.LoyaltyCard;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoyaltyCardTests {

    @Test
    public void testGetPoints() {
        LoyaltyCard card = new LoyaltyCard();
        assertEquals(java.util.Optional.of(card.getPoints()),java.util.Optional.of(0));
    }
    @Test
    public void testSetPoints() {
        LoyaltyCard card = new LoyaltyCard();
        card.setPoints(20);
        assertEquals(java.util.Optional.of(card.getPoints()),java.util.Optional.of(20));
    }
    @Test
    public void testGetCustomer() {
        CustomerImpl customer = new CustomerImpl("name");
        LoyaltyCard card = new LoyaltyCard("123",customer,100);
        assertEquals(card.getCustomer(),customer);
    }
    @Test
    public void testSetCustomer() {
        CustomerImpl customer = new CustomerImpl("name");
        LoyaltyCard card = new LoyaltyCard("123");
        card.setCustomer(customer);
        assertEquals(card.getCustomer(),customer);
    }
    @Test
    public void testGetCardId() {
        LoyaltyCard card = new LoyaltyCard("123",null,100);
        assertEquals(card.getCardId(),"123");
    }
    @Test
    public void testSetCardId() {
        LoyaltyCard card = new LoyaltyCard("123");
        card.setCardId("456");
        assertEquals(card.getCardId(),"456");
    }
}