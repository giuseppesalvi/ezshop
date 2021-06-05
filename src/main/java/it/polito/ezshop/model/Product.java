package it.polito.ezshop.model;

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

    public String getRFID() {
        return RFID;
    }
}
