package it.polito.ezshop.model;

import it.polito.ezshop.data.Order;

public class OrderImpl implements Order {

    static public Integer idGen = 1;
    private ProductTypeImpl product;
    private String status;
    private Integer quantity;
    private Integer id;
    private Double pricePerUnit;
    private Integer balanceId;

    //Constructor for new orders
    public OrderImpl(ProductTypeImpl product, Integer quantity, Double pricePerUnit) {
        this.product = product;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.status = "ISSUED";
        this.balanceId = null;
        this.id = idGen++;
    }

    //Constructor for orders with already an ID
    public OrderImpl(ProductTypeImpl product, Integer quantity, Double pricePerUnit,
                     String status, Integer balanceId, Integer id) {
        this.product = product;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.status = status;
        this.balanceId = balanceId;
        this.id = id;
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
        //Not implemented since we have a reference, not a string
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

    public ProductTypeImpl getProduct(){
        return this.product;
    }

    public void setProduct(ProductTypeImpl product) {
        this.product = product;
    }
}
