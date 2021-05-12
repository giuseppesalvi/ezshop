package it.polito.ezshop.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.ezshop.data.TicketEntry;

public class ReturnTransaction {
	public static Integer idGen = 1;
	private Integer returnID;
	private SaleTransactionImpl transaction;
	private List<TicketEntryImpl> products;
	private String state;
	private boolean commit;
	private Double value;

	public ReturnTransaction(SaleTransactionImpl transaction) {
		this.returnID = idGen++;
		this.transaction = transaction;
		this.products = new ArrayList<TicketEntryImpl>();
		this.state = "OPEN";
		this.commit = false;
		this.value = 0.0;
	}

	public ReturnTransaction(Integer returnID, SaleTransactionImpl transaction, List<TicketEntryImpl> products,
			String state, boolean commit, Double value) {
		this.returnID = returnID;
		this.transaction = transaction;
		this.products = products;
		this.state = state;
		this.commit = commit;
		this.value = value;
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

	public List<TicketEntryImpl> getProducts() {
		return products;
	}

	public void setProducts(List<TicketEntryImpl> products) {
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

	public boolean addEntry(TicketEntryImpl entry) {
		return this.products.add(entry);
	}
	
	public TicketEntry deleteEntry(String barcode) {
		int targetIdx = -1;
		for (TicketEntry p : products) {
			if (p.getBarCode().contentEquals(barcode)) {
				targetIdx = products.indexOf(p);
			}
		}
		
		if (targetIdx == -1) {
			return null;
		}
		TicketEntry eliminated = products.get(targetIdx);
		products.remove(targetIdx);
		return eliminated;
	}

}
