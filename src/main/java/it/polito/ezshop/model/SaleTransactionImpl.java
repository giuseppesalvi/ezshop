package it.polito.ezshop.model;

import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.TicketEntry;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SaleTransactionImpl implements SaleTransaction {
	
	private static Integer idGen = 1;
	private Integer transactionID;
	private List<TicketEntry> products;
	private Double globalDiscountRate;
	private String state;
	private LocalDate date;
	private LocalTime time;
	private Double cost;
	private String paymentType;
	private CreditCard creditCard;

    public SaleTransactionImpl() {
		this.transactionID = idGen++;
		this.products = null;
		this.globalDiscountRate = null;
		this.state = "OPEN";
		this.date = LocalDate.now();
		this.time = LocalTime.now();
		this.cost = null;
		this.paymentType = null;
		this.creditCard = null;
	}

	@Override
    public Integer getTicketNumber() {
        return this.transactionID;
    }

    @Override
    public void setTicketNumber(Integer ticketNumber) {
    	this.transactionID = ticketNumber;
    }

    @Override
    public List<TicketEntry> getEntries() {
        return this.products;
    }

    @Override
    public void setEntries(List<TicketEntry> entries) {
    	this.products = entries;
    }

    @Override
    public double getDiscountRate() {
        return this.globalDiscountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
    	this.globalDiscountRate = discountRate;
    }

    @Override
    public double getPrice() {
        return this.cost;
    }

    @Override
    public void setPrice(double price) {
    	this.cost = price;
    }

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public boolean addEntry(TicketEntry entry) {
		if (this.products == null) {
			this.products = new ArrayList<TicketEntry>();
		}
		return this.products.add(entry);
	}
}
