package it.polito.ezshop.model;

public class LoyaltyCard {

    public static Integer idGen = 1;
    private String cardId;
    private CustomerImpl customer;
    private Integer points;

    //Constructor for new loyalty card
    public LoyaltyCard() {
        this.cardId = String.format("%010d", idGen++);
        this.customer = null;
        this.points = 0;
    }

    //Constructor for loyalty card with already an ID
    public LoyaltyCard(String cardId, CustomerImpl customer, Integer points) {
        this.cardId = cardId;
        this.customer = customer;
        this.points = points;
    }

    //Dummy LoyaltyCard
    public LoyaltyCard(String cardId){
        this.customer = null;
        this.points = null;
        this.cardId = cardId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public CustomerImpl getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerImpl customer) {
        //Trying to set the same customer, do nothing
        if (this.customer != null){
            if (this.customer.equals(customer))
                return;
        }

        this.customer = customer;

        //Null is valid
        if (customer != null) {
            if (customer.getCard() != null && !this.cardId.equals(customer.getCustomerCard())) {
                customer.getCard().setCustomer(null);
            }
            customer.setCard(this);
        }
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
