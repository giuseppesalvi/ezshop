package it.polito.ezshop.model;

import it.polito.ezshop.data.ProductType;

public class ProductTypeImpl implements ProductType {

    static public Integer idGen = 1;
    private String productCode;
    private String description;
    private Double sellPrice;
    private String notes;
    private Integer id;
    private Integer quantity;
    private Position position;

    public ProductTypeImpl(String productCode, String description, Double sellPrice, String notes) {
        this.productCode = productCode;
        this.description = description;
        this.sellPrice = sellPrice;
        this.notes = notes;
        this.quantity = 0;
        this.position = new Position(" ", " ", " ");
        this.id = idGen++;
    }

    public ProductTypeImpl(String productCode, String description, Double sellPrice,
                           String notes, Integer quantity, String position, Integer id) {
        this.productCode = productCode;
        this.description = description;
        this.sellPrice = sellPrice;
        this.notes = notes;
        this.quantity = quantity;
        this.position = new Position(position);
        this.id = id;
    }

    //dummy product
    public ProductTypeImpl(Integer id){
        this.id = id;
        this.productCode = null;
        this.description = null;
        this.sellPrice = null;
        this.notes = null;
        this.quantity = null;
        this.position = null;
    }
     public ProductTypeImpl(String productCode){
        this.id = null;
        this.productCode = productCode;
        this.description = null;
        this.sellPrice = null;
        this.notes = null;
        this.quantity = null;
        this.position = null;
    }   
    //dummy product with barcode
    

     public static boolean checkBarCode(String barCode) {

        if (barCode.matches("^[0-9]{12,14}$")) {
            int sum = 0;
            boolean x3 = true;
            for (int i = barCode.length() - 2; i >= 0; i--, x3 = !x3) {
                sum += (x3) ? Character.getNumericValue(barCode.charAt(i)) * 3 : Character.getNumericValue( barCode.charAt(i));
            }
            sum = (10 - (sum % 10)) % 10;
            if (sum == (Character.getNumericValue(barCode.charAt(barCode.length()-1))))
                return true;
        }

        return false;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getLocation() {
        return position.getPosition();
    }

    @Override
    public void setLocation(String location) {
        position = new Position(location);
    }

    @Override
    public String getNote() {
        return notes;
    }

    @Override
    public void setNote(String note) {
        this.notes = note;
    }

    @Override
    public String getProductDescription() {
        return description;
    }

    @Override
    public void setProductDescription(String productDescription) {
        this.description = productDescription;
    }

    @Override
    public String getBarCode() {
        return productCode;
    }

    @Override
    public void setBarCode(String barCode) {
        this.productCode = barCode;
    }

    @Override
    public Double getPricePerUnit() {
        return sellPrice;
    }

    @Override
    public void setPricePerUnit(Double pricePerUnit) {
        this.sellPrice = pricePerUnit;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
