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
		this.globalDiscountRate = 0.0;
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
    	// Compute cost and set it
    	setPrice(computeCost());
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
	
	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public boolean addEntry(TicketEntry entry) {
		if (this.products == null) {
			this.products = new ArrayList<TicketEntry>();
		}
		return this.products.add(entry);
	}
	
	public boolean deleteEntry(String barcode) {
		int targetIdx = -1;
		for (TicketEntry p : products) {
			if (p.getBarCode().contentEquals(barcode)) {
				targetIdx = products.indexOf(p);
			}
		}
		
		if (targetIdx == -1) {
			return false;
		}

		products.remove(targetIdx);
		return true;
	}
	
	public Double computeCost() {
		Double cost = 0.0;
		for (TicketEntry prod : products) {
			// For each product add its price after discount * its quantity
			cost += prod.getPricePerUnit() * (1.00 - prod.getDiscountRate() ) * prod.getAmount();
		}
		// Apply sale DiscountRate
		cost = cost * (1.00 - globalDiscountRate);
		return cost;
	}
	
}
