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

	public static boolean checkValidity(String creditCard) {
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
	}
	
	
}
