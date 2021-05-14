package it.polito.ezshop.model;

import it.polito.ezshop.data.EZShopMaps;
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

    //Constructor for new products
    public ProductTypeImpl(String productCode, String description, Double sellPrice, String notes) {
        this.productCode = productCode;
        this.description = description;
        this.sellPrice = sellPrice;
        this.notes = notes;
        this.quantity = 0;
        this.position = new Position(" ", " ", " ");
        this.id = idGen++;
    }

    //Constructor for products with already an ID
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

    //dummy product with id
    public ProductTypeImpl(Integer id){
        this.id = id;
        this.productCode = null;
        this.description = null;
        this.sellPrice = null;
        this.notes = null;
        this.quantity = null;
        this.position = null;
    }

    //dummy product with barcode
     public ProductTypeImpl(String productCode){
        this.id = null;
        this.productCode = productCode;
        this.description = null;
        this.sellPrice = null;
        this.notes = null;
        this.quantity = null;
        this.position = null;
    }   

     public static boolean checkBarCode(String barCode) {
        if (barCode != null && barCode.matches("^[0-9]{12,14}$")) {
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
        if (quantity != null && quantity > 0)
            this.quantity = quantity;
    }

    @Override
    public String getLocation() {
        return position.getPosition();
    }

    @Override
    public void setLocation(String location) {
        if (location != null && !location.isEmpty() && location.matches(".+-.+-.+"))
            //Test if the position is equal to the current one
            if (position != null && !position.getPosition().contentEquals(location))
                //Uniqueness
                if (EZShopMaps.products.values().stream().noneMatch(p -> p.getLocation().contentEquals(location)))
                    position = new Position(location);
    }

    @Override
    public String getNote() {
        return notes;
    }

    @Override
    public void setNote(String note) {
        //Note can be equal to null
        this.notes = note;
    }

    @Override
    public String getProductDescription() {
        return description;
    }

    @Override
    public void setProductDescription(String productDescription) {
        if (productDescription != null && !productDescription.isEmpty())
            this.description = productDescription;
    }

    @Override
    public String getBarCode() {
        return productCode;
    }

    @Override
    public void setBarCode(String barCode) {
        if (barCode != null && !barCode.isEmpty() && !this.productCode.equals(barCode)){
            if (checkBarCode(barCode))
                this.productCode = barCode;
        }
    }

    @Override
    public Double getPricePerUnit() {
        return sellPrice;
    }

    @Override
    public void setPricePerUnit(Double pricePerUnit) {
        if (pricePerUnit!= null && pricePerUnit > 0)
            this.sellPrice = pricePerUnit;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        if (id != null && !EZShopMaps.products.containsKey(id)){
            this.id = id;
            if (id > idGen)
                idGen = id+1;
        }
    }
}
