package it.polito.ezshop.model;

import it.polito.ezshop.data.EZShopMaps;
import it.polito.ezshop.data.FileWrite;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;

import java.util.Optional;

public class OrderImpl implements Order {

    static public Integer idGen = 1;
    private ProductType product;
    private String status;
    private Integer quantity;
    private Integer id;
    private Double pricePerUnit;
    private Integer balanceId;

    //Constructor for new orders
    public OrderImpl(ProductType product, Integer quantity, Double pricePerUnit) {
        this.product = product;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.status = "ISSUED";
        this.balanceId = null;
        this.id = idGen++;
    }

    //Constructor for orders with already an ID
    public OrderImpl(ProductType product, Integer quantity, Double pricePerUnit,
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
        if (balanceId != null && !EZShopMaps.operations.containsKey(balanceId)){
            EZShopMaps.operations.get(this.balanceId).setBalanceId(balanceId);
            this.balanceId = balanceId;
            FileWrite.writeOperations(EZShopMaps.operations);
            FileWrite.writeOrders(EZShopMaps.orders);
        }
    }

    @Override
    public String getProductCode() {
        return product.getBarCode();
    }

    @Override
    public void setProductCode(String productCode) {
        if (productCode != null && !productCode.isEmpty()){
            Optional<ProductTypeImpl> prod = EZShopMaps.products.values().stream()
                    .filter(p -> p.getBarCode().contentEquals(productCode)).findFirst();
            prod.ifPresent(productType -> this.product = productType);
            FileWrite.writeOrders(EZShopMaps.orders);
        }
    }

    @Override
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    @Override
    public void setPricePerUnit(double pricePerUnit) {
        if (pricePerUnit > 0) {
            this.pricePerUnit = pricePerUnit;
            FileWrite.writeOrders(EZShopMaps.orders);
        }
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        if (quantity > 0) {
            this.quantity = quantity;
            FileWrite.writeOrders(EZShopMaps.orders);
        }
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        if (status != null &&
                (status.contentEquals("ISSUED") ||
                    status.contentEquals("PAYED") ||
                    status.contentEquals("COMPLETED"))){
            this.status = status;
            FileWrite.writeOrders(EZShopMaps.orders);
        }
    }

    @Override
    public Integer getOrderId() {
        return id;
    }

    @Override
    public void setOrderId(Integer orderId) {
        if (orderId != null && !EZShopMaps.orders.containsKey(orderId)){
            this.id = orderId;
            if (orderId > idGen)
                idGen = orderId+1;
            FileWrite.writeOrders(EZShopMaps.orders);
        }
    }

    public ProductType getProduct(){
        return this.product;
    }

    public void setProduct(ProductType product) {
        if (product != null) {
            this.product = product;
            FileWrite.writeOrders(EZShopMaps.orders);
        }
    }
}
