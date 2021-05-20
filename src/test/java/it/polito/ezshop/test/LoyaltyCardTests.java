package it.polito.ezshop.test;

import it.polito.ezshop.model.CustomerImpl;
import it.polito.ezshop.model.LoyaltyCard;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoyaltyCardTests {

    @Test
    public void testGettersSettersConstructors() {
        LoyaltyCard card = new LoyaltyCard();
        // setGetPoints
        card.setPoints(20);
        assertEquals(java.util.Optional.of(card.getPoints()),java.util.Optional.of(20));
        // setGetCustomer
        CustomerImpl customer = new CustomerImpl("name");
        card.setCustomer(customer);
        assertEquals(card.getCustomer(),customer);
        // setGetCardId
        card.setCardId("456");
        assertEquals(card.getCardId(),"456");
        
        LoyaltyCard card2 = new LoyaltyCard("0000000006", customer, 10);
        LoyaltyCard card3 = new LoyaltyCard("0000000008");
        assertNotNull(card2);
        assertNotNull(card3);
    }
}