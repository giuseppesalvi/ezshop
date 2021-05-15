package it.polito.ezshop.model;

import it.polito.ezshop.data.EZShopMaps;
import it.polito.ezshop.data.FileWrite;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.TicketEntry;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SaleTransactionImpl implements SaleTransaction {
	
	public static Integer idGen = 1;
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
		this.products = new ArrayList<TicketEntry>();
		this.globalDiscountRate = 0.0;
		this.state = "OPEN";
		this.date = LocalDate.now();
		this.time = LocalTime.now();
		this.cost = 0.0;
		this.paymentType = null;
		this.creditCard = null;
	}

    public SaleTransactionImpl(Integer transactionId, List<TicketEntry> products,
							   Double globalDiscountRate, String state, String dateString,
							   String timeString, Double cost, String paymentType, String creditCard) {
		this.transactionID = transactionId;
		this.products = products;
		this.globalDiscountRate = globalDiscountRate;
		this.state = state;
		if(dateString == null) {
			this.date = null;
		}
		else {
			this.date = LocalDate.parse(dateString);
		}
		if (timeString == null)
			this.time = null;
		else {
			this.time = LocalTime.parse(timeString);
		}
		this.cost = cost;
		this.paymentType = paymentType;
		this.creditCard = new CreditCard(creditCard, null);
	}


	@Override
    public Integer getTicketNumber() {
        return this.transactionID;
    }

    @Override
    public void setTicketNumber(Integer ticketNumber) {
    	this.transactionID = ticketNumber;
    	if (ticketNumber > 0 && !EZShopMaps.sales.containsKey(ticketNumber)) {
			if (ticketNumber > idGen)
				idGen = ticketNumber + 1;
			this.transactionID = ticketNumber;
			FileWrite.writeSales(EZShopMaps.sales);
		}

    }

    @Override
    public List<TicketEntry> getEntries() {
        return this.products;
    }

    @Override
    public void setEntries(List<TicketEntry> entries) {
    	if (entries == null) {
    		this.products = new ArrayList<TicketEntry>();
    	}
    	else {
    		this.products = entries;
    	}
    	FileWrite.writeSales(EZShopMaps.sales);
    }

    @Override
    public double getDiscountRate() {
        return this.globalDiscountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
    	if (discountRate >= 0 && discountRate < 1) {
    		this.globalDiscountRate = discountRate;
    		FileWrite.writeSales(EZShopMaps.sales);
    	}
    }

    @Override
    public double getPrice() {
    	// Compute cost and set it
    	setPrice(computeCost());
        return this.cost;
    }

    @Override
    public void setPrice(double price) {
    	if (price > 0) {
    		this.cost = price;
    		FileWrite.writeSales(EZShopMaps.sales);
    	}
    }

	public String getState() {
		return state;
	}

	public void setState(String state) {
		if (state != null && (state == "OPEN" || state == "CLOSED" || state == "PAYED")) {
			this.state = state;
			FileWrite.writeSales(EZShopMaps.sales);
		}
	}
	
	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		if (state == null || state == "cash" || state == "creditCard") {
			this.paymentType = paymentType;
			FileWrite.writeSales(EZShopMaps.sales);
		}

	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
		FileWrite.writeSales(EZShopMaps.sales);
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		if (date != null) {
			this.date = date;
			FileWrite.writeSales(EZShopMaps.sales);
		}
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		if (time != null) {
			this.time = time;
			FileWrite.writeSales(EZShopMaps.sales);
		}

	}

	public boolean addEntry(TicketEntry entry) {
		 this.products.add(entry);
		return FileWrite.writeSales(EZShopMaps.sales);
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
 		FileWrite.writeSales(EZShopMaps.sales);
		return eliminated;
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
