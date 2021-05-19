package it.polito.ezshop.test;

import it.polito.ezshop.model.CustomerImpl;
import it.polito.ezshop.model.LoyaltyCard;

import org.junit.Test;
import static org.junit.Assert.*;

public class CustomerImplTests {

    @Test
    public void testSettersGetters() {
        CustomerImpl customer = new CustomerImpl("name");
        // setGetCustomerName
        customer.setCustomerName("newName");
        assertEquals(customer.getCustomerName(), "newName");
        // setGetId
        customer.setId(2);
        assertEquals(java.util.Optional.of(customer.getId()) , java.util.Optional.of(2));
        // setGetCard
        LoyaltyCard card = new LoyaltyCard("123");
        customer.setCard(card);
        assertEquals(customer.getCard(),card);
        // setGetCustomerCard
        customer.setCustomerCard("158");
        assertEquals(customer.getCard().getCardId(), "158");
        // setGetPoints
        customer.setPoints(100);
        assertEquals(java.util.Optional.of(customer.getPoints()),java.util.Optional.of(100));
    }
}
