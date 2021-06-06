package it.polito.ezshop.model;

import it.polito.ezshop.data.ProductType;

public class Product {
    private ProductTypeImpl productType;
    private String RFID;

    public Product(ProductTypeImpl productType, String RFID) {
        this.productType = productType;
        this.RFID = RFID;
    }

    public ProductTypeImpl getProductType() {
        return productType;
    }

    public void setProductType(ProductTypeImpl p) {
        this.productType = p;
    }

    public String getRFID() {
        return RFID;
    }
}
