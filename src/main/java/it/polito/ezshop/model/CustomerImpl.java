package it.polito.ezshop.model;

import it.polito.ezshop.data.Customer;

public class CustomerImpl implements Customer {

    public static Integer idGen = 1;
    private String customerName;
    private Integer customerId;
    private LoyaltyCard card;

    //Constructor for new customers
    public CustomerImpl(String customerName) {
        this.customerName = customerName;
        this.card = null;
        this.customerId = idGen++;
    }

    //Constructor for customers with already an ID
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
        //DO NOT USE
        this.card.setCardId(customerCard);;
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
            card.setPoints(points);
    }

    public LoyaltyCard getCard() {
        return card;
    }

    public void setCard(LoyaltyCard card) {
        //Trying to set the same card, do nothing
        if (this.card != null){
            if (this.card.equals(card))
                return;
        }

        this.card = card;

        //Null is valid
        if (card != null) {
            if (card.getCustomer() != null && !this.customerId.equals(card.getCustomer().getId())) {
                card.getCustomer().setCard(null);
            }
            card.setCustomer(this);
        }
    }
}
