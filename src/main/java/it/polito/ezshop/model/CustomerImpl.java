package it.polito.ezshop.model;

import it.polito.ezshop.data.Customer;

public class CustomerImpl implements Customer {

    private static Integer idGen = 1;
    private String customerName;
    private Integer customerId;
    private LoyaltyCard card;

    public CustomerImpl(String customerName) {
        this.customerName = customerName;
        this.card = null;
        this.customerId = idGen++;
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
        return card.getCardId();
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
        return card.getPoints();
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
