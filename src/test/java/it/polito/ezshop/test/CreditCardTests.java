package it.polito.ezshop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.polito.ezshop.model.CreditCard;

public class CreditCardTests {

	// Black box tests
	
	@Test
	public void testCheckValidityWithInvalidString() {
		assertFalse(CreditCard.checkValidity(null));
		assertFalse(CreditCard.checkValidity(""));
	}

	@Test
	public void testCheckValidityWithCreditCardNotANumber() {
		assertFalse(CreditCard.checkValidity("012ab45"));
	}

	@Test
	public void testCheckValidityLuhnCheckPassed() {
		assertTrue(CreditCard.checkValidity("4485370086510891"));
	}

	@Test
	public void testCheckValidityLuhnCheckNotPassed() {
		assertFalse(CreditCard.checkValidity("4485370086510892"));
	}
	
	// White box tests
	
	@Test
	public void testGettersSettersConstructors() {
		CreditCard card = new CreditCard("4485370086510891", 100.00);
		assertNotNull(card);
		assertEquals(card.getNumber(),"4485370086510891");
		assertEquals(card.getBalance(), (Double)100.00);
		card.setNumber("4485370086510892");
		card.setBalance(90.00);
		assertEquals(card.getNumber(),"4485370086510892");
		assertEquals(card.getBalance(), (Double)90.00);
	}
	
	@Test 
	public void testCheckValidityForLoop0Iterations() {
		assertFalse(CreditCard.checkValidity(""));
	}
	
	@Test 
	public void testCheckValidityForLoop1Iterations() {
		assertFalse(CreditCard.checkValidity("1"));
		assertFalse(CreditCard.checkValidity("2"));
		assertFalse(CreditCard.checkValidity("3"));
		assertFalse(CreditCard.checkValidity("4"));
		assertFalse(CreditCard.checkValidity("5"));
		assertFalse(CreditCard.checkValidity("6"));
		assertFalse(CreditCard.checkValidity("7"));
		assertFalse(CreditCard.checkValidity("8"));
		assertFalse(CreditCard.checkValidity("9"));
		assertTrue(CreditCard.checkValidity("0"));
	}
	
	@Test 
	public void testCheckValidityForLoopMultipleIterations() {
		assertTrue(CreditCard.checkValidity("4485370086510891"));
		
	}
	
}
