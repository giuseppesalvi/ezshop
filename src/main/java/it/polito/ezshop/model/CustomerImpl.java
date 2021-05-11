package it.polito.ezshop.model;

import it.polito.ezshop.data.Customer;

public class CustomerImpl implements Customer {

    public static Integer idGen = 1;
    private String customerName;
    private Integer customerId;
    private LoyaltyCard card;

    public CustomerImpl(String customerName) {
        this.customerName = customerName;
        this.card = null;
        this.customerId = idGen++;
    }

    public CustomerImpl(String customerName, Integer customerId, LoyaltyCard card) {
        this.customerName = customerName;
        this.customerId = customerId;
        this.card = card;
    }

    //Dummy customer
    public CustomerImpl(Integer id) {
        this.customerName = null;
        this.card = null;
        this.customerId = id;
    }

    @Override
    public String getCustomerName() {
        return customerName;
    }

    @Override
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public String getCustomerCard() {
        return (card != null)? card.getCardId(): "";
    }

    @Override
    public void setCustomerCard(String customerCard) {
        this.card.setCardId(customerCard);
    }

    @Override
    public Integer getId() {
        return customerId;
    }

    @Override
    public void setId(Integer id) {
        this.customerId = id;
    }

    @Override
    public Integer getPoints() {
        return (card!=null)?card.getPoints():0;
    }

    @Override
    public void setPoints(Integer points) {
        this.card.setPoints(points);
    }

    public LoyaltyCard getCard() {
        return card;
    }

    public void setCard(LoyaltyCard card) {
        this.card = card;
    }
}
