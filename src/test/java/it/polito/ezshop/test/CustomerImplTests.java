package it.polito.ezshop.test;

import it.polito.ezshop.model.CustomerImpl;
import it.polito.ezshop.model.LoyaltyCard;

import org.junit.Test;
import static org.junit.Assert.*;

public class CustomerImplTests {

    @Test
    public void testGettersSettersConstructors() {
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
        
        CustomerImpl customer2 = new CustomerImpl(124);
        CustomerImpl customer3 = new CustomerImpl("John", 130, null);
        assertNotNull(customer2);
        assertNotNull(customer3);
        assertEquals(customer2.getCustomerCard(), "");
        assertEquals(customer2.getPoints(), (Integer)0);
    }
}
