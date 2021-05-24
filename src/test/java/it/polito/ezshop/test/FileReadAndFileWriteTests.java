package it.polito.ezshop.test;
import it.polito.ezshop.data.*;
import it.polito.ezshop.model.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.List;


public class FileReadAndFileWriteTests {
    EZShopInterface UUT = new EZShop();

    @Test
    public void testReadAndWriteUsers() {
        HashMap<Integer, UserImpl> currentUsers = FileRead.readUsers();
        UserImpl newUser = new UserImpl("newUser","newPass","Cashier");
        currentUsers.putIfAbsent(newUser.getId(),newUser);
        assertTrue(FileWrite.writeUsers(currentUsers));
        HashMap<Integer, UserImpl> readUsers = FileRead.readUsers();
        assertEquals( readUsers.get(newUser.getId()).getId(),newUser.getId());
        assertEquals( readUsers.get(newUser.getId()).getUsername(),"newUser");
        assertEquals( readUsers.get(newUser.getId()).getPassword(),"newPass");
        assertEquals( readUsers.get(newUser.getId()).getRole(),"Cashier");
        UUT.reset();
    }
    @Test
    public void testReadWriteProducts() {
        HashMap<Integer, ProductTypeImpl> currentProducts = FileRead.readProducts();
        ProductTypeImpl productType = new ProductTypeImpl("6291041500213","Coffee",12.0,"Arabica");
        currentProducts.putIfAbsent(productType.getId(),productType);
        assertTrue(FileWrite.writeProducts(currentProducts));
        HashMap<Integer, ProductTypeImpl> readProducts = FileRead.readProducts();
        assertEquals(readProducts.get(productType.getId()).getId(),productType.getId());
        assertEquals(readProducts.get(productType.getId()).getProductDescription(),productType.getProductDescription());
        assertEquals(readProducts.get(productType.getId()).getPricePerUnit(),productType.getPricePerUnit());
        assertEquals(readProducts.get(productType.getId()).getBarCode(),productType.getBarCode());
        UUT.reset();
    }
    @Test
    public void testReadWriteOrders() {
        HashMap<Integer, OrderImpl> currentOrders = FileRead.readOrders();
        ProductTypeImpl productType = new ProductTypeImpl("6291041500213","Coffee",12.0,"Arabica");
        OrderImpl order = new OrderImpl(productType,1,12.0);
        currentOrders.putIfAbsent(order.getOrderId(),order);
        assertTrue(FileWrite.writeOrders(currentOrders));
        HashMap<Integer, OrderImpl> readOrders = FileRead.readOrders();
        assertEquals(readOrders.get(order.getOrderId()).getOrderId(),order.getOrderId());
        assertEquals(readOrders.get(order.getOrderId()).getBalanceId(),order.getBalanceId());
        assertEquals(readOrders.get(order.getOrderId()).getQuantity(),order.getQuantity());
        UUT.reset();
    }
    @Test
    public void testReadWriteCustomers() {
        HashMap<Integer, CustomerImpl> currentCustomers = FileRead.readCustomers();
        CustomerImpl customer = new CustomerImpl("newCustomer");
        currentCustomers.putIfAbsent(customer.getId(),customer);
        assertTrue(FileWrite.writeCustomers(currentCustomers));
        HashMap<Integer, CustomerImpl> readCustomers = FileRead.readCustomers();
        assertEquals(readCustomers.get(customer.getId()).getId(),customer.getId());
        assertEquals(readCustomers.get(customer.getId()).getCustomerName(),customer.getCustomerName());
        UUT.reset();
    }
    @Test
    public void testReadWriteCards() {
        HashMap<String, LoyaltyCard> currentCards = FileRead.readCards();
        LoyaltyCard card = new LoyaltyCard();
        currentCards.putIfAbsent(card.getCardId(),card);
        assertTrue(FileWrite.writeCards(currentCards));
        HashMap<String, LoyaltyCard> readCards = FileRead.readCards();
        assertEquals(readCards.get(card.getCardId()).getCardId(),card.getCardId());
        UUT.reset();
    }
    @Test
    public void testReadWriteSales() {
        HashMap<Integer, SaleTransactionImpl> currentSales = FileRead.readSales();
        SaleTransactionImpl saleTransaction = new SaleTransactionImpl();
        currentSales.putIfAbsent(saleTransaction.getTicketNumber(),saleTransaction);
        assertTrue(FileWrite.writeSales(currentSales));
        HashMap<Integer, SaleTransactionImpl> readSales = FileRead.readSales();
        assertEquals(readSales.get(saleTransaction.getTicketNumber()).getTicketNumber(),saleTransaction.getTicketNumber());
        UUT.reset();
    }
    @Test
    public void testReadWriteReturns() {
        HashMap<Integer, ReturnTransaction> currentReturns = FileRead.readReturns();
        SaleTransactionImpl saleTransaction = new SaleTransactionImpl();
        ReturnTransaction returnTransaction = new ReturnTransaction(saleTransaction);
        currentReturns.putIfAbsent(returnTransaction.getReturnID(),returnTransaction);
        assertTrue(FileWrite.writeReturns(currentReturns));
        HashMap<Integer, ReturnTransaction> readReturns = FileRead.readReturns();
        assertEquals(readReturns.get(returnTransaction.getReturnID()).getReturnID(),returnTransaction.getReturnID());
        UUT.reset();
    }
    @Test
    public void testReadWriteOperations() {
        HashMap<Integer, BalanceOperationImpl> currentOperations = FileRead.readOperations();
        BalanceOperationImpl balanceOperation = new BalanceOperationImpl(10.0,"DEBT");
        currentOperations.putIfAbsent(balanceOperation.getBalanceId(),balanceOperation);
        assertTrue(FileWrite.writeOperations(currentOperations));
        HashMap<Integer, BalanceOperationImpl> readOperations = FileRead.readOperations();
        assertEquals(readOperations.get(balanceOperation.getBalanceId()).getBalanceId(),balanceOperation.getBalanceId());
        UUT.reset();
    }
    @Test
    public void testReadWriteCreditCards() {
        List<CreditCard> currentCreditCardList = FileRead.readCreditCards();
        CreditCard card = new CreditCard("1122330086610898",100.0);
        currentCreditCardList.add(card);
        assertTrue(FileWrite.writeCreditCards(currentCreditCardList));
        List<CreditCard> readCreditCardList = FileRead.readCreditCards();
        assertEquals(readCreditCardList.get(readCreditCardList.size()-1).getNumber(),card.getNumber());
        UUT.reset();
    }
}