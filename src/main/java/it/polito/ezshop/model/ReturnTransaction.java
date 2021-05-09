package it.polito.ezshop.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.data.TicketEntry;

public class ReturnTransaction {
	private static Integer idGen = 1;
	private Integer returnID;
	private SaleTransactionImpl transaction;
	private List<TicketEntry> products;
	private String state;
	private boolean commit;
	private Double value;

	public ReturnTransaction(SaleTransactionImpl transaction) {
		this.returnID = idGen++;
		this.transaction = transaction;
		this.products = null;
		this.state = "OPEN";
		this.commit = false;
		this.value = null;
	}

	public Integer getReturnID() {
		return returnID;
	}

	public void setReturnID(Integer returnID) {
		this.returnID = returnID;
	}

	public SaleTransactionImpl getTransaction() {
		return transaction;
	}

	public void setTransaction(SaleTransactionImpl transaction) {
		this.transaction = transaction;
	}

	public List<TicketEntry> getProducts() {
		return products;
	}

	public void setProducts(List<TicketEntry> products) {
		this.products = products;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isCommit() {
		return commit;
	}

	public void setCommit(boolean commit) {
		this.commit = commit;
	}

	public Double getValue() {
		// Compute value and set it
		setValue(computeValue());
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	public Double computeValue() {
		Double amount = 0.0;
		for (TicketEntry prod : products) {
			// For each product add its price after discount * its quantity
			amount += prod.getPricePerUnit() * (1.00 - prod.getDiscountRate() ) * prod.getAmount();
		}
		// Apply sale DiscountRate
		amount = amount * (1.00 - transaction.getDiscountRate());
		return amount;
	}

	public boolean addEntry(TicketEntry entry) {
		if (this.products == null) {
			this.products = new ArrayList<TicketEntry>();
		}
		return this.products.add(entry);
	}

}
