package it.polito.ezshop.model;

import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;

public class OrderImpl implements Order {

    static private Integer idGen = 1;
    private ProductType product;
    private String status;
    private Integer quantity;
    private Integer id;
    private Double pricePerUnit;
    private Integer balanceId;

    public OrderImpl(ProductType product, Integer quantity, Double pricePerUnit) {
        this.product = product;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.status = "issued";
        this.balanceId = null;
        this.id = idGen++;
    }

    @Override
    public Integer getBalanceId() {
        return balanceId;
    }

    @Override
    public void setBalanceId(Integer balanceId) {
        this.balanceId = balanceId;
    }

    @Override
    public String getProductCode() {
        return product.getBarCode();
    }

    @Override
    public void setProductCode(String productCode) {
        this.product.setBarCode(productCode);
    }

    @Override
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    @Override
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Integer getOrderId() {
        return id;
    }

    @Override
    public void setOrderId(Integer orderId) {
        this.id = orderId;
    }

    public ProductType getProduct(){
        return this.product;
    }
}
