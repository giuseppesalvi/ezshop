package it.polito.ezshop.model;

import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.TicketEntry;

public class TicketEntryImpl implements TicketEntry {
	
	private ProductType product;
	private Integer quantity;
	private Double discountRate;

	public TicketEntryImpl(ProductType product, Integer quantity) {
		this.product = product;
		this.quantity = quantity;
		this.discountRate = 0.0;
	}

	public TicketEntryImpl(ProductType product, Integer quantity, Double discountRate) {
		this.product = product;
		this.quantity = quantity;
		this.discountRate = discountRate;
	}

    @Override
    public String getBarCode() {
        return this.product.getBarCode();
    }

   	@Override
    public void setBarCode(String barCode) {
    	this.product.setBarCode(barCode);
    }

    @Override
    public String getProductDescription() {
        return this.product.getProductDescription();
    }

    @Override
    public void setProductDescription(String productDescription) {
    	this.product.setProductDescription(productDescription);
    }

    @Override
    public int getAmount() {
        return this.quantity;
    }

    @Override
    public void setAmount(int amount) {
    	this.quantity = amount;
    }

    @Override
    public double getPricePerUnit() {
        return this.product.getPricePerUnit();
    }

    @Override
    public void setPricePerUnit(double pricePerUnit) {
    	this.product.setPricePerUnit(pricePerUnit);
    }

    @Override
    public double getDiscountRate() {
        return this.discountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
    	this.discountRate = discountRate;
    }
}
