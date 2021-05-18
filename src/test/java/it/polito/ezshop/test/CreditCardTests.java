package it.polito.ezshop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.security.InvalidParameterException;

import org.junit.Test;

import it.polito.ezshop.exceptions.InvalidCreditCardException;
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
	
	@Test
	public void testSetNumberWithInvalidString() throws InvalidCreditCardException {
		CreditCard card = new CreditCard("4485370086510891", 100.00);
		assertThrows(InvalidCreditCardException.class, () -> card.setNumber(null));
	}
	
	@Test 
	public void testSetNumberWithValidString() throws InvalidCreditCardException {
		CreditCard card = new CreditCard("4485370086510891", 100.00);
		card.setNumber("5100293991053009");
		assertEquals(card.getNumber(), "5100293991053009");
	}
	
	@Test 
	public void testSetBalanceWithNull() throws InvalidParameterException{
		CreditCard card = new CreditCard("4485370086510891", 100.00);
		assertThrows(InvalidParameterException.class, () -> card.setBalance(null));
	}
	
	@Test 
	public void testSetBalanceWithValidBalance() throws InvalidParameterException{
		CreditCard card = new CreditCard("4485370086510891", 100.00);
		card.setBalance(50.00);
		assertEquals(card.getBalance(), (Double) 50.00);
	}
	
	// White box tests
	
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
