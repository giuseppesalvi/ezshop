package it.polito.ezshop.data;

import it.polito.ezshop.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class EZShopMaps {
    static public HashMap<Integer, UserImpl> users;
    static public HashMap<Integer, SaleTransactionImpl> sales;
    static public HashMap<Integer, ProductTypeImpl> products;
    static public HashMap<Integer, OrderImpl> orders;
    static public HashMap<Integer, BalanceOperationImpl> operations;
    static public HashMap<Integer, ReturnTransaction> returns;
    static public HashMap<Integer, CustomerImpl> customers;
    static public HashMap<String, LoyaltyCard> cards;
    static public User loggedInUser;

    static public void eraseMaps(){
        users.clear();
        UserImpl.idGen = 1;
        FileWrite.writeUsers(EZShopMaps.users);

        sales.clear();
        SaleTransactionImpl.idGen = 1;
        FileWrite.writeSales(EZShopMaps.sales);

        products.clear();
        ProductTypeImpl.idGen = 1;
        FileWrite.writeProducts(EZShopMaps.products);

        orders.clear();
        OrderImpl.idGen = 1;
        FileWrite.writeOrders(EZShopMaps.orders);

        operations.clear();
        BalanceOperationImpl.idGen = 1;
        FileWrite.writeOperations(EZShopMaps.operations);

        returns.clear();
        ReturnTransaction.idGen = 1;
        FileWrite.writeReturns(EZShopMaps.returns);

        customers.clear();
        CustomerImpl.idGen = 1;
        FileWrite.writeCustomers(EZShopMaps.customers);

        cards.clear();
        LoyaltyCard.idGen = 1;
        FileWrite.writeCards(EZShopMaps.cards);
    }
    
    static public void loadMaps(){
        EZShopMaps.users = FileRead.readUsers();
        EZShopMaps.sales = FileRead.readSales();
        EZShopMaps.products = FileRead.readProducts();
        EZShopMaps.orders = FileRead.readOrders();
        EZShopMaps.operations = FileRead.readOperations();
        EZShopMaps.returns = FileRead.readReturns();
        EZShopMaps.customers = FileRead.readCustomers();
        EZShopMaps.cards = FileRead.readCards();
        EZShopMaps.loggedInUser = null;

        // Restore references between classes

        // Customer -> EZShopMaps.cards
        for (CustomerImpl cus : EZShopMaps.customers.values()) {
            if (cus.getCard() != null && !cus.getCard().getCardId().equals("")) {
                cus.setCard(EZShopMaps.cards.get(cus.getCard().getCardId()));
            } else {
                cus.setCard(null);
            }

        }

        // Cards -> customer
        for (LoyaltyCard loy : EZShopMaps.cards.values()) {
            if (loy.getCustomer() != null && loy.getCustomer().getId() != -1) {
                loy.setCustomer(EZShopMaps.customers.get(loy.getCustomer().getId()));
            } else {
                loy.setCustomer(null);
            }
        }

        // EZShopMaps.orders -> productType
        for (OrderImpl ord : EZShopMaps.orders.values()) {
            ord.setProduct(EZShopMaps.products.get(ord.getProduct().getId()));
        }

        // EZShopMaps.sales -> productType
        for (SaleTransactionImpl sal : EZShopMaps.sales.values()) {
            List<TicketEntry> newEntries = new ArrayList<>();
            for (TicketEntry ent : sal.getEntries()) {
                // Retrieve the product from the map
                Optional<ProductTypeImpl> pro = EZShopMaps.products.values().stream()
                        .filter(p -> p.getBarCode().contentEquals(ent.getBarCode())).findFirst();

                newEntries.add(new TicketEntryImpl(pro.get(), ent.getAmount(), ent.getDiscountRate()));
            }
            sal.setEntries(newEntries);
        }

        // EZShopMaps.returns -> productType
        for (ReturnTransaction ret : EZShopMaps.returns.values()) {
            for (TicketEntryImpl ent : ret.getProducts()) {
                // Retrieve the product from the map
                Optional<ProductTypeImpl> pro = EZShopMaps.products.values().stream()
                        .filter(p -> p.getBarCode().contentEquals(ent.getBarCode())).findFirst();
                ent.setProduct(pro.get());
            }
        }

        // EZShopMaps.returns -> EZShopMaps.sales
        for (ReturnTransaction ret : EZShopMaps.returns.values()) {
            ret.setTransaction(EZShopMaps.sales.get(ret.getTransaction().getTicketNumber()));
        }
    }
}
