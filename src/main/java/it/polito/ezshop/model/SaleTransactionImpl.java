package it.polito.ezshop.model;

import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.TicketEntry;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SaleTransactionImpl implements SaleTransaction {
	
	public static Integer idGen = 1;
	private Integer transactionID;
	private List<TicketEntry> products;
	private List<Product> productsRFID;
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
		this.productsRFID = new ArrayList<Product>();
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
							   String timeString, Double cost, String paymentType, String creditCard,
							   List<Product> productsRFID) {
		this.transactionID = transactionId;
		this.productsRFID = productsRFID;
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
		if (creditCard == null)
			this.creditCard = null;
		else
			this.creditCard = new CreditCard(creditCard, null);
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
    	if (entries == null) {
    		this.products = new ArrayList<TicketEntry>();
    	}
    	else {
    		this.products = entries;
    	}
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

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public boolean addEntry(TicketEntry entry) {
		if (entry == null) {
			return false;
		}
		else {
			this.products.add(entry);
			return true;
		}
	}
	
	public TicketEntry deleteEntry(String barcode) {
		int targetIdx = -1;
		if (barcode == null) {
			return null;
		}
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
	
	public Double computeCost() {
		Double cost = 0.0;
		for (TicketEntry prod : products) {
			// For each product add its price after discount * its quantity
			cost += prod.getPricePerUnit() * (1.00 - prod.getDiscountRate() ) * prod.getAmount();
		}
		if (productsRFID != null) {
			for (Product prod : productsRFID) {
				// Check if this productType has a discount
				Optional<TicketEntry> t = products.stream().filter(a -> a.getBarCode().contentEquals(prod.getProductType().getBarCode())).findFirst();
				if (t.isPresent()) {
					cost += prod.getProductType().getPricePerUnit() * (1.00 - t.get().getDiscountRate());
				} else {
					cost += prod.getProductType().getPricePerUnit();
				}
			}
		}
		// Apply sale DiscountRate
		cost = cost * (1.00 - globalDiscountRate);
		return cost;
	}

	public void setProductsRFID(List<Product> productsRFID) {
		this.productsRFID = productsRFID;
	}

	public List<Product> getProductsRFID() {
		return productsRFID;
	}

	public boolean addProductsRFID(Product prod) {
		if (prod == null) {
			return false;
		}
		else {
			this.productsRFID.add(prod);
			return true;
		}
	}

	public Product deleteProductsRFID(String RFID) {
		int targetIdx = -1;
		if (RFID == null) {
			return null;
		}
		for (Product p : productsRFID) {
			if (p.getRFID().contentEquals(RFID)) {
				targetIdx = productsRFID.indexOf(p);
			}
		}

		if (targetIdx == -1) {
			return null;
		}
		Product eliminated = productsRFID.get(targetIdx);
		productsRFID.remove(targetIdx);
		return eliminated;
	}
}
