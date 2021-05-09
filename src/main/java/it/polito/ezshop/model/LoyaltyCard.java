package it.polito.ezshop.model;

public class LoyaltyCard {

    private static Integer idGen = 1;
    private String cardId;
    private CustomerImpl customer;
    private Integer points;

    public LoyaltyCard() {
        this.cardId = String.format("%010d", idGen++);
        this.customer = null;
        this.points = 0;
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
