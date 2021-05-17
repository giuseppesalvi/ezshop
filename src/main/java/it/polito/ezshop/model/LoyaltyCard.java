package it.polito.ezshop.model;

import it.polito.ezshop.data.EZShopMaps;

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
        if (points != null && points > 0)
            this.points = points;
    }

    public CustomerImpl getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerImpl customer) {
        if (customer != null){
            if (customer.getCard() != null) {
                customer.getCard().setCustomer(null);
            }
            customer.setCard(this);
            this.customer = customer;
        }
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        if (cardId != null && !EZShopMaps.cards.containsKey(cardId)){
            this.cardId = cardId;
            if (Integer.parseInt(cardId) > idGen)
                idGen = Integer.parseInt(cardId)+1;
        }
    }
}
