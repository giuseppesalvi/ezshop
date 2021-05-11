package it.polito.ezshop.model;

public class LoyaltyCard {

    public static Integer idGen = 1;
    private String cardId;
    private CustomerImpl customer;
    private Integer points;

    public LoyaltyCard() {
        this.cardId = String.format("%010d", idGen++);
        this.customer = null;
        this.points = 0;
    }

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
        this.customer = customer;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
