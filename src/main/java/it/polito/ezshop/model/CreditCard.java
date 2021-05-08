package it.polito.ezshop.model;

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

	public void setNumber(String number) {
		this.number = number;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public static boolean checkValidity() {
		// TODO
		return true;
	}
	
	
}
