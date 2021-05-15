package it.polito.ezshop.model;

import java.util.Optional;

import it.polito.ezshop.data.EZShopMaps;
import it.polito.ezshop.data.TicketEntry;

public class TicketEntryImpl implements TicketEntry {

	private ProductTypeImpl product;
	private Integer quantity;
	private Double discountRate;

	public TicketEntryImpl(ProductTypeImpl product, Integer quantity) {
		this.product = product;
		this.quantity = quantity;
		this.discountRate = 0.0;
	}

	public TicketEntryImpl(ProductTypeImpl product, Integer quantity, Double discountRate) {
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
		if (barCode != null) {
			// Retrieve the correct product
			Optional<ProductTypeImpl> prod = EZShopMaps.products.values().stream()
					.filter(p -> p.getBarCode().contentEquals(barCode)).findFirst();

			// Check if the product exists in the map
			if (prod.isPresent()) {
				this.product = prod.get();
			}
		}

	}

	@Override
	public String getProductDescription() {
		return this.product.getProductDescription();
	}

	@Override
	public void setProductDescription(String productDescription) {
		if (productDescription != null) {
			this.product.setProductDescription(productDescription);
		}
	}

	@Override
	public int getAmount() {
		return this.quantity;
	}

	@Override
	public void setAmount(int amount) {
		if (amount >= 0) {
			this.quantity = amount;
		}
	}

	@Override
	public double getPricePerUnit() {
		return this.product.getPricePerUnit();
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		if (pricePerUnit > 0) {
			this.product.setPricePerUnit(pricePerUnit);
		}
	}

	@Override
	public double getDiscountRate() {
		return this.discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		if (discountRate >= 0 && discountRate < 1) {
			this.discountRate = discountRate;
		}
	}

	public ProductTypeImpl getProduct() {
		return product;
	}

	public void setProduct(ProductTypeImpl product) {
		if (product != null) {
			this.product = product;
		}
	}
}
