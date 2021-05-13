package it.polito.ezshop.test;

import org.junit.Test;

import it.polito.ezshop.model.CreditCard;

public class CreditCardTests {

	// Black box tests

	@Test
	public void testCheckValidityWithInvalidString() {
		assert (CreditCard.checkValidity(null) == false);
		assert (CreditCard.checkValidity("") == false);
	}

	@Test
	public void testCheckValidityWithCreditCardNotANumber() {
		assert (CreditCard.checkValidity("012ab45") == false);
	}

	@Test
	public void testCheckValidityLuhnCheckPassed() {
		assert (CreditCard.checkValidity("4485370086510891") == true);
	}

	@Test
	public void testCheckValidityLuhnCheckNotPassed() {
		assert (CreditCard.checkValidity("4485370086510892") == false);
	}
	
	// White box tests
	
	

}
