package it.polito.ezshop.model;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.EZShopMaps;
import it.polito.ezshop.data.FileWrite;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;

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
        if (customerName != null && !customerName.isEmpty())
            this.customerName = customerName;
    }

    @Override
    public String getCustomerCard() {
        return (card != null)? card.getCardId(): "";
    }

    @Override
    public void setCustomerCard(String customerCard) {
        if (customerCard != null && customerCard.matches("^[0-9]{10}$")) {
            if (EZShopMaps.cards.containsKey(customerCard)){
                //if card has already a customer attached I should detach it
                if (EZShopMaps.cards.get(customerCard).getCustomer() != null) {
                    EZShopMaps.cards.get(customerCard).getCustomer().setCustomerCard(null);
                }

                // Double reference
                this.card = EZShopMaps.cards.get(customerCard);
                EZShopMaps.cards.get(customerCard).setCustomer(this);
            }
        }
    }

    @Override
    public Integer getId() {
        return customerId;
    }

    @Override
    public void setId(Integer id) {
        if (id != null && !EZShopMaps.customers.containsKey(id)){
            this.customerId = id;
            if (id > idGen)
                idGen = id+1;
        }
    }

    @Override
    public Integer getPoints() {
        return (card!=null)?card.getPoints():0;
    }

    @Override
    public void setPoints(Integer points) {
        if (this.card != null && points!= null && points > 0)
            this.card.setPoints(points);
    }

    public LoyaltyCard getCard() {
        return card;
    }

    public void setCard(LoyaltyCard card) {
        if (card != null){
            if (card.getCustomer() != null){
                card.getCustomer().setCard(null);
            }
            this.card = card;
            card.setCustomer(this);
        }
    }
}
