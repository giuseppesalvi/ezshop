package it.polito.ezshop.model;

import java.security.InvalidParameterException;

import it.polito.ezshop.exceptions.InvalidCreditCardException;

public class CreditCard {

	private String number;
	private Double balance;

	public CreditCard(String number, Double balance) {
		this.number = number;
		this.balance = balance;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) throws InvalidCreditCardException {
		if (!checkValidity(number)) {
			throw new InvalidCreditCardException();
		} else {
			this.number = number;
		}

	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) throws InvalidParameterException{
		if (balance != null)
			this.balance = balance;
		else
			throw new InvalidParameterException();
	}

	/* This method applies Luhn algorithm to verify if a number is valid */
	public static boolean checkValidity(String creditCard) {

		if (creditCard == null || creditCard.isEmpty())
			// creditCard is an invalid String
			return false;

		try {
			// Luhn algorithm
			int sum = 0;
			boolean alternate = false;
			for (int i = creditCard.length() - 1; i >= 0; i--) {
				int n = Integer.parseInt(creditCard.substring(i, i + 1));
				if (alternate) {
					n *= 2;
					if (n > 9) {
						n = (n % 10) + 1;
					}
				}
				sum += n;
				alternate = !alternate;
			}
			return (sum % 10 == 0);
		} catch (NumberFormatException e) {
			// creditCard is not a number
			return false;

		}
	}

}
