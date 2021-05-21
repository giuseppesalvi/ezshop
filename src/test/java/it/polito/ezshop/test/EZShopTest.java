package it.polito.ezshop.test;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class EZShopTest {
    EZShopInterface UUT;

    @Before
    public void initialize() {
        UUT = new EZShop();
    }

    private void createandlog(int level) throws InvalidPasswordException,
            InvalidRoleException, InvalidUsernameException {
        String role;
        switch (level){
            case 0:
                role = "Cashier";
                break;
            case 1:
                role = "ShopManager";
                break;
            default:
                role = "Administrator";
        }
        assertNotEquals((int)UUT.createUser(role, role, role), -1);
        assertNotNull(UUT.login(role, role));
    }

    // --------------- issueOrder --------------- //
    @Test
    public void testIssueOrderNominalCase() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException,
            InvalidProductDescriptionException, InvalidPricePerUnitException,
            InvalidProductCodeException, InvalidQuantityException{
        UUT.reset();
        createandlog(2); //Admin
        int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
        assertNotEquals(-1, productID);
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(-1, orderID);
    }

    @Test
    public void testIssueOrderUnauthorized() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException{
        UUT.reset();
        //No user logged
        assertThrows(UnauthorizedException.class,
                () -> UUT.issueOrder("6291041500213", 12, 10.0));
        createandlog(0); //Cashier
        assertThrows(UnauthorizedException.class,
                () -> UUT.issueOrder("6291041500213", 12, 10.0));
    }

    @Test
    public void testIssueOrderInvalidProductCode() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException{
        UUT.reset();
        createandlog(1); //ShopManager
        assertThrows(InvalidProductCodeException.class,
                () -> UUT.issueOrder("6291041230213", 12, 10.0));
        assertThrows(InvalidProductCodeException.class,
                () -> UUT.issueOrder(null, 12, 10.0));
        assertThrows(InvalidProductCodeException.class,
                () -> UUT.issueOrder("", 12, 10.0));
    }

    @Test
    public void testIssueOrderInvalidQuantity() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException{
        UUT.reset();
        createandlog(1); //ShopManager
        assertThrows(InvalidQuantityException.class,
                () -> UUT.issueOrder("6291041230213", -12, 10.0));
        assertThrows(InvalidQuantityException.class,
                () -> UUT.issueOrder("6291041230213", 0, 10.0));
    }

    @Test
    public void testIssueOrderInvalidPricePerUnit() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException{
        UUT.reset();
        createandlog(1); //ShopManager
        assertThrows(InvalidPricePerUnitException.class,
                () -> UUT.issueOrder("6291041230213", 12, -10.0));
        assertThrows(InvalidPricePerUnitException.class,
                () -> UUT.issueOrder("6291041230213", 12, 0.0));
    }

    @Test
    public void testIssueOrderProductNotExists() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, InvalidQuantityException,
            UnauthorizedException, InvalidPricePerUnitException,
            InvalidProductCodeException {
        UUT.reset();
        createandlog(1); //ShopManager
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertEquals(-1, orderID);
    }
    // --------------- end issueOrder --------------- //

    // --------------- payOrderFor --------------- //
    @Test
    public void testPayOrderForNominalCase() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException,
            InvalidProductDescriptionException, InvalidPricePerUnitException,
            InvalidProductCodeException, InvalidQuantityException{
        UUT.reset();
        createandlog(2); //Admin
        int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
        assertNotEquals(-1, productID);
        assertTrue(UUT.recordBalanceUpdate(1200.0));
        int orderID = UUT.payOrderFor("6291041500213", 12, 10.0);
        assertNotEquals(-1, orderID);
    }

    @Test
    public void testPayOrderForUnauthorized() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException{
        UUT.reset();
        //No user logged
        assertThrows(UnauthorizedException.class,
                () -> UUT.payOrderFor("6291041500213", 12, 10.0));
        createandlog(0); //Cashier
        assertThrows(UnauthorizedException.class,
                () -> UUT.payOrderFor("6291041500213", 12, 10.0));
    }

    @Test
    public void testPayOrderForInvalidProductCode() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException{
        UUT.reset();
        createandlog(1); //ShopManager
        assertThrows(InvalidProductCodeException.class,
                () -> UUT.payOrderFor("6291041230213", 12, 10.0));
        assertThrows(InvalidProductCodeException.class,
                () -> UUT.payOrderFor(null, 12, 10.0));
        assertThrows(InvalidProductCodeException.class,
                () -> UUT.payOrderFor("", 12, 10.0));
    }

    @Test
    public void testPayOrderForInvalidQuantity() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException{
        UUT.reset();
        createandlog(1); //ShopManager
        assertThrows(InvalidQuantityException.class,
                () -> UUT.payOrderFor("6291041230213", -12, 10.0));
        assertThrows(InvalidQuantityException.class,
                () -> UUT.payOrderFor("6291041230213", 0, 10.0));
    }

    @Test
    public void testPayOrderForInvalidPricePerUnit() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException{
        UUT.reset();
        createandlog(1); //ShopManager
        assertThrows(InvalidPricePerUnitException.class,
                () -> UUT.payOrderFor("6291041230213", 12, -10.0));
        assertThrows(InvalidPricePerUnitException.class,
                () -> UUT.payOrderFor("6291041230213", 12, 0.0));
    }

    @Test
    public void testPayOrderForProductNotExists() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, InvalidQuantityException,
            UnauthorizedException, InvalidPricePerUnitException,
            InvalidProductCodeException {
        UUT.reset();
        createandlog(1); //ShopManager
        int orderID = UUT.payOrderFor("6291041500213", 12, 10.0);
        assertEquals(-1, orderID);
    }

    @Test
    public void testPayOrderForBalanceNotEnough() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException,
            InvalidProductDescriptionException, InvalidPricePerUnitException,
            InvalidProductCodeException, InvalidQuantityException{
        UUT.reset();
        createandlog(2); //Admin
        int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
        assertNotEquals(-1, productID);
        int orderID = UUT.payOrderFor("6291041500213", 12, 10.0);
        assertEquals(-1, orderID);
    }
    // --------------- end payOrderFor --------------- //

    // --------------- payOrder --------------- //
    @Test
    public void testPayOrderNominalCase() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException,
            InvalidProductDescriptionException, InvalidPricePerUnitException,
            InvalidProductCodeException, InvalidQuantityException,
            InvalidOrderIdException {
        UUT.reset();
        createandlog(2); //Admin
        int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
        assertNotEquals(-1, productID);
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(-1, orderID);
        assertTrue(UUT.recordBalanceUpdate(1200.0));
        assertTrue(UUT.payOrder(orderID));
        assertEquals(1080.0, UUT.computeBalance(), 0.001);
    }

    @Test
    public void testPayOrderUnauthorized() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException{
        UUT.reset();
        //No user logged
        assertThrows(UnauthorizedException.class,
                () -> UUT.payOrder(12));
        createandlog(0); //Cashier
        assertThrows(UnauthorizedException.class,
                () -> UUT.payOrder(12));
    }

    @Test
    public void testPayOrderNotExists() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException,
            InvalidOrderIdException {
        UUT.reset();
        createandlog(2); //Admin
        assertFalse(UUT.payOrder(12));
    }

    @Test
    public void testPayOrderInvalidOrderID() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException{
        UUT.reset();
        createandlog(2); //Admin
        assertThrows(InvalidOrderIdException.class,() -> UUT.payOrder(null));
    }

    @Test
    public void testPayOrderBalanceNotEnough() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException,
            InvalidProductDescriptionException, InvalidPricePerUnitException,
            InvalidProductCodeException, InvalidQuantityException,
            InvalidOrderIdException {
        UUT.reset();
        createandlog(2); //Admin
        int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
        assertNotEquals(-1, productID);
        assertTrue(UUT.recordBalanceUpdate(119.0));
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(-1, orderID);
        assertFalse(UUT.payOrder(orderID));
    }

    @Test
    public void testPayOrderAlreadyReceived() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException,
            InvalidProductDescriptionException, InvalidPricePerUnitException,
            InvalidProductCodeException, InvalidQuantityException,
            InvalidLocationException, InvalidProductIdException,
            InvalidOrderIdException {
        UUT.reset();
        createandlog(2); //Admin
        int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
        assertNotEquals(-1, productID);
        assertTrue(UUT.recordBalanceUpdate(120.0));
        int orderID = UUT.payOrderFor("6291041500213", 12, 10.0);
        assertNotEquals(-1, orderID);
        assertTrue(UUT.updatePosition(productID, "123-abc-123"));
        assertTrue(UUT.recordOrderArrival(orderID));
        assertFalse(UUT.payOrder(orderID));
    }

    @Test
    public void testPayOrderAlreadyPayed() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException,
            InvalidProductDescriptionException, InvalidPricePerUnitException,
            InvalidProductCodeException, InvalidQuantityException,
            InvalidOrderIdException {
        UUT.reset();
        createandlog(2); //Admin
        int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
        assertNotEquals(-1, productID);
        assertTrue(UUT.recordBalanceUpdate(240.0));
        int orderID = UUT.payOrderFor("6291041500213", 12, 10.0);
        assertNotEquals(-1, orderID);
        assertTrue(UUT.payOrder(orderID));
        assertEquals(120.0, UUT.computeBalance(), 0.001);
    }
    // --------------- end payOrder --------------- //

    // --------------- recordOrderArrival --------------- //
    @Test
    public void testRecordOrderArrivalNominalCase() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException,
            InvalidProductDescriptionException, InvalidPricePerUnitException,
            InvalidProductCodeException, InvalidQuantityException,
            InvalidOrderIdException, InvalidLocationException,
            InvalidProductIdException {
        UUT.reset();
        createandlog(2); //Admin
        int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
        assertNotEquals(-1, productID);
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(-1, orderID);
        assertTrue(UUT.recordBalanceUpdate(120.0));
        assertTrue(UUT.payOrder(orderID));
        assertEquals(0.00, UUT.computeBalance(), 0.001);
        assertTrue(UUT.updatePosition(productID, "123-abc-123"));
        assertTrue(UUT.recordOrderArrival(orderID));
        ProductType pr = UUT.getProductTypeByBarCode("6291041500213");
        assertNotNull(pr);
        assertEquals(12, (int)pr.getQuantity());
    }

    @Test
    public void testRecordOrderArrivalUnauthorized() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException{
        UUT.reset();
        //No user logged
        assertThrows(UnauthorizedException.class,
                () -> UUT.recordOrderArrival(12));
        createandlog(0); //Cashier
        assertThrows(UnauthorizedException.class,
                () -> UUT.recordOrderArrival(12));
    }

    @Test
    public void testRecordOrderArrivalInvalidOrderID() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException,
            InvalidOrderIdException, InvalidLocationException {
        UUT.reset();
        createandlog(2); //Admin
        assertThrows(InvalidOrderIdException.class,() -> UUT.recordOrderArrival(null));
        assertThrows(InvalidOrderIdException.class,() -> UUT.recordOrderArrival(0));
        assertFalse(UUT.recordOrderArrival(12));
    }

    @Test
    public void testRecordOrderArrivalLocationNotExists() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException,
            InvalidProductDescriptionException, InvalidPricePerUnitException,
            InvalidProductCodeException, InvalidQuantityException,
            InvalidOrderIdException{
        UUT.reset();
        createandlog(2); //Admin
        int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
        assertNotEquals(-1, productID);
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(-1, orderID);
        assertTrue(UUT.recordBalanceUpdate(120.0));
        assertTrue(UUT.payOrder(orderID));
        assertEquals(0.00, UUT.computeBalance(), 0.001);
        assertThrows(InvalidLocationException.class, () -> UUT.recordOrderArrival(orderID));
    }

    @Test
    public void testRecordOrderArrivalAlreadyArrived() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException,
            InvalidProductDescriptionException, InvalidPricePerUnitException,
            InvalidProductCodeException, InvalidQuantityException,
            InvalidOrderIdException, InvalidLocationException,
            InvalidProductIdException {
        UUT.reset();
        createandlog(2); //Admin
        int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
        assertNotEquals(-1, productID);
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(-1, orderID);
        assertTrue(UUT.recordBalanceUpdate(120.0));
        assertTrue(UUT.payOrder(orderID));
        assertEquals(0.00, UUT.computeBalance(), 0.001);
        assertTrue(UUT.updatePosition(productID, "123-abc-123"));
        assertTrue(UUT.recordOrderArrival(orderID));
        ProductType pr = UUT.getProductTypeByBarCode("6291041500213");
        assertNotNull(pr);
        assertEquals(12, (int)pr.getQuantity());

        assertTrue(UUT.recordOrderArrival(orderID));
        assertEquals(12, (int)pr.getQuantity()); //Quantity does not change
    }

    @Test
    public void testRecordOrderArrivalNotPayed() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException,
            InvalidProductDescriptionException, InvalidPricePerUnitException,
            InvalidProductCodeException, InvalidQuantityException,
            InvalidOrderIdException, InvalidLocationException,
            InvalidProductIdException {
        UUT.reset();
        createandlog(2); //Admin
        int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
        assertNotEquals(-1, productID);
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(-1, orderID);
        assertTrue(UUT.recordBalanceUpdate(120.0));
        assertTrue(UUT.updatePosition(productID, "123-abc-123"));
        assertFalse(UUT.recordOrderArrival(orderID));
    }
    // --------------- end recordOrderArrival --------------- //

    // --------------- getAllOrders --------------- //
    @Test
    public void testGetAllOrdersNominalCase() throws InvalidRoleException, InvalidPasswordException,
            InvalidUsernameException, UnauthorizedException,
            InvalidProductDescriptionException, InvalidPricePerUnitException,
            InvalidProductCodeException, InvalidQuantityException {
        UUT.reset();
        createandlog(2); //Admin
        int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
        assertNotEquals(-1, productID);
        productID = UUT.createProductType("Apple", "563491053110", 2.0, "Machintosh");
        assertNotEquals(-1, productID);

        //Test with 0 orders
        List<Order> orders = UUT.getAllOrders();
        assertEquals(0, orders.size());

        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(-1, orderID);
        orderID = UUT.issueOrder("563491053110", 33, 10.0);
        assertNotEquals(-1, orderID);

        //Test with 2 orders
        orders = UUT.getAllOrders();
        assertEquals(2, orders.size());
    }

    @Test
    public void testGetAllOrdersUnauthorized() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException{
        UUT.reset();
        //No user logged
        assertThrows(UnauthorizedException.class,
                () -> UUT.getAllOrders());
        createandlog(0); //Cashier
        assertThrows(UnauthorizedException.class,
                () -> UUT.getAllOrders());
    }
    // --------------- end getAllOrders --------------- //

    // --------------- defineCustomer --------------- //
    @Test
    public void testDefineCustomerNominalCase() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, InvalidCustomerNameException,
            UnauthorizedException {
        UUT.reset();
        createandlog(0); //Cashier
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);
    }

    @Test
    public void testDefineCustomerUnauthorized()  {
        UUT.reset();
        assertThrows(UnauthorizedException.class, () -> UUT.defineCustomer("Piero Rossi"));
    }

    @Test
    public void testDefineCustomerInvalidName() throws InvalidPasswordException,
            InvalidRoleException, InvalidUsernameException {
        UUT.reset();
        createandlog(0); //Cashier
        assertThrows(InvalidCustomerNameException.class, () -> UUT.defineCustomer(""));
        assertThrows(InvalidCustomerNameException.class, () -> UUT.defineCustomer(null));
    }

    @Test
    public void testDefineCustomerAlreadyExists() throws InvalidPasswordException,
            InvalidRoleException, InvalidUsernameException,
            InvalidCustomerNameException, UnauthorizedException {
        UUT.reset();
        createandlog(0); //Cashier
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);
        assertEquals(-1, (int)UUT.defineCustomer("Piero Rossi"));
    }
    // --------------- end defineCustomer --------------- //

    // --------------- modifyCustomer --------------- //
    @Test
    public void testModifyCustomerNominalCase() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, InvalidCustomerNameException,
            UnauthorizedException, InvalidCustomerIdException,
            InvalidCustomerCardException {
        UUT.reset();
        createandlog(0); //Cashier

        //New customer
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);

        //New card
        String cardID = UUT.createCard();
        assertNotEquals("", cardID);

        //Modifies data
        assertTrue(UUT.modifyCustomer(customerID, "Gianni Primo", cardID));

        //Check data
        Customer testCustomer = UUT.getCustomer(customerID);
        assertEquals("Gianni Primo", testCustomer.getCustomerName());
        assertEquals(cardID, testCustomer.getCustomerCard());
    }

    @Test
    public void testModifyCustomerUnauthorized() {
        UUT.reset();
        assertThrows(UnauthorizedException.class, () -> UUT.modifyCustomer(12, "", ""));
    }

    @Test
    public void testModifyCustomerInvalidName() throws InvalidPasswordException,
            InvalidRoleException, InvalidUsernameException,
            InvalidCustomerNameException, UnauthorizedException {
        UUT.reset();
        createandlog(0); //Cashier

        //New customer
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);

        assertThrows(InvalidCustomerNameException.class,
                () -> UUT.modifyCustomer(customerID, "", ""));
        assertThrows(InvalidCustomerNameException.class,
                () -> UUT.modifyCustomer(customerID, null, ""));
    }

    @Test
    public void testModifyCustomerInvalidID() throws InvalidPasswordException,
            InvalidRoleException, InvalidUsernameException {
        UUT.reset();
        createandlog(0); //Cashier

        assertThrows(InvalidCustomerIdException.class,
                () -> UUT.modifyCustomer(0, "Gianni", ""));
        assertThrows(InvalidCustomerIdException.class,
                () -> UUT.modifyCustomer(null, "Gianni", ""));
    }

    @Test
    public void testModifyCustomerInvalidCard() throws InvalidPasswordException,
            InvalidRoleException, InvalidUsernameException,
            InvalidCustomerNameException, UnauthorizedException {
        UUT.reset();
        createandlog(0); //Cashier

        //New customer
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);

        //Null and empty are allowed
        assertThrows(InvalidCustomerCardException.class,
                () -> UUT.modifyCustomer(customerID, "Piero Rossi", "0123"));

    }

    @Test
    public void testModifyCustomerCustomerNotExists() throws InvalidPasswordException,
            InvalidRoleException, InvalidUsernameException,
            InvalidCustomerNameException, UnauthorizedException,
            InvalidCustomerIdException, InvalidCustomerCardException {
        UUT.reset();
        createandlog(0); //Cashier

        assertFalse(UUT.modifyCustomer(12, "Gianni pino", "0000000001"));
    }

    @Test
    public void testModifyCustomerModifyCard() throws InvalidPasswordException,
            InvalidRoleException, InvalidUsernameException,
            InvalidCustomerNameException, UnauthorizedException,
            InvalidCustomerIdException, InvalidCustomerCardException {
        UUT.reset();
        createandlog(0); //Cashier

        //New customer
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);

        //New card
        String cardID = UUT.createCard();
        assertNotEquals("", cardID);

        //Attach card to customer
        assertTrue(UUT.modifyCustomer(customerID, "Gianni Primo", cardID));

        //Check data
        Customer testCustomer = UUT.getCustomer(customerID);
        assertEquals("Gianni Primo", testCustomer.getCustomerName());
        assertEquals(cardID, testCustomer.getCustomerCard());

        //Null as cardID, should remain the same
        assertTrue(UUT.modifyCustomer(customerID, "Gianni Primo", null));

        //Check
        assertEquals(cardID, testCustomer.getCustomerCard());

        //Empty as cardID, should detach the card
        assertTrue(UUT.modifyCustomer(customerID, "Gianni Primo", ""));

        //Check
        assertEquals("", testCustomer.getCustomerCard());

        //New customer 2
        int customerID2 = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID2);

        //New card
        String cardID2 = UUT.createCard();
        assertNotEquals("", cardID2);

        //Attach card2 to customer2
        assertTrue(UUT.modifyCustomer(customerID2, "Piero Rossi", cardID2));

        //Try to attach card2 to customer1, should return false since the card
        //has already a customer attached
        assertFalse(UUT.modifyCustomer(customerID, "Gianni Primo", cardID2));

        //Check
        assertEquals("", testCustomer.getCustomerCard());

        //Try to modify the name of customer1 with a name already present, should return false
        assertFalse(UUT.modifyCustomer(customerID, "Piero Rossi", null));

        //Check
        assertEquals("Gianni Primo", testCustomer.getCustomerName());
    }
    // --------------- end modifyCustomer --------------- //

    // --------------- deleteCustomer --------------- //
    @Test
    public void testDeleteCustomerNominalCase() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, InvalidCustomerNameException,
            UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
        UUT.reset();
        createandlog(0); //Cashier
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);

        String cardID = UUT.createCard();
        assertNotEquals("", cardID);

        assertTrue(UUT.attachCardToCustomer(cardID, customerID));

        //Delete
        assertTrue(UUT.deleteCustomer(customerID));

        //Check
        List<Customer> customers = UUT.getAllCustomers();
        assertEquals(0, customers.size());
    }

    @Test
    public void testDeleteCustomerUnauthorized() {
        UUT.reset();
        assertThrows(UnauthorizedException.class, () -> UUT.deleteCustomer(12));
    }

    @Test
    public void testDeleteCustomerInvalidID() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, InvalidCustomerNameException,
            UnauthorizedException {
        UUT.reset();
        createandlog(0); //Cashier
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);
        assertThrows(InvalidCustomerIdException.class, () -> UUT.deleteCustomer(0));
        assertThrows(InvalidCustomerIdException.class, () -> UUT.deleteCustomer(-100));
        assertThrows(InvalidCustomerIdException.class, () -> UUT.deleteCustomer(null));
    }

    @Test
    public void testDeleteCustomerCustomerNotExists() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, InvalidCustomerNameException,
            UnauthorizedException, InvalidCustomerIdException {
        UUT.reset();
        createandlog(0); //Cashier
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);
        assertFalse(UUT.deleteCustomer(customerID+1));
    }
    // --------------- end deleteCustomer --------------- //

    // --------------- getCustomer --------------- //
    @Test
    public void testGetCustomerNominalCase() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, InvalidCustomerNameException,
            UnauthorizedException, InvalidCustomerIdException {
        UUT.reset();
        createandlog(0); //Cashier
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);

        assertNotEquals(-1, (int)UUT.defineCustomer("Paolo Rossi"));
        assertNotEquals(-1, (int)UUT.defineCustomer("Gianni Rossi"));

        Customer testCustomer = UUT.getCustomer(customerID);
        assertNotNull(testCustomer);

        //Check
        assertEquals("Piero Rossi", testCustomer.getCustomerName());
    }

    @Test
    public void testGetCustomerUnauthorized(){
        UUT.reset();
        assertThrows(UnauthorizedException.class, () -> UUT.getCustomer(12));
    }

    @Test
    public void testGetCustomerInvalidID() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, InvalidCustomerNameException,
            UnauthorizedException {
        UUT.reset();
        createandlog(0); //Cashier
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);
        assertThrows(InvalidCustomerIdException.class, () -> UUT.getCustomer(0));
        assertThrows(InvalidCustomerIdException.class, () -> UUT.getCustomer(-100));
        assertThrows(InvalidCustomerIdException.class, () -> UUT.getCustomer(null));
    }

    @Test
    public void testGetCustomerCustomerNotExists() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, InvalidCustomerNameException,
            UnauthorizedException, InvalidCustomerIdException {
        UUT.reset();
        createandlog(0); //Cashier
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);
        assertNull(UUT.getCustomer(customerID+1));
    }
    // --------------- end getCustomer --------------- //

    // --------------- getAllCustomers --------------- //
    @Test
    public void testGetAllCustomersNominalCase() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, InvalidCustomerNameException,
            UnauthorizedException {
        UUT.reset();
        createandlog(0); //Cashier
        assertEquals(0, UUT.getAllCustomers().size());

        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);
        assertEquals(1, UUT.getAllCustomers().size());

        assertNotEquals(-1, (int)UUT.defineCustomer("Paolo Rossi"));
        assertNotEquals(-1, (int)UUT.defineCustomer("Gianni Rossi"));
        assertEquals(3, UUT.getAllCustomers().size());
    }

    @Test
    public void testGetAllCustomerUnauthorized(){
        UUT.reset();
        assertThrows(UnauthorizedException.class, () -> UUT.getAllCustomers());
    }
    // --------------- end getAllCustomers --------------- //

    // --------------- createCard --------------- //
    @Test
    public void testCreateCardNominalCase() throws InvalidPasswordException,
            InvalidRoleException, InvalidUsernameException,
            UnauthorizedException {
        UUT.reset();
        createandlog(0); //Cashier
        String cardID = UUT.createCard();
        assertNotNull(cardID);
        assertEquals(10, cardID.length());
    }

    @Test
    public void testCreateCardUnauthorized(){
        UUT.reset();
        assertThrows(UnauthorizedException.class, () -> UUT.createCard());
    }
    // --------------- end createCard --------------- //

    // --------------- attachCardToCustomer --------------- //
    @Test
    public void testAttachCardToCustomerNominalCase() throws InvalidPasswordException,
            InvalidRoleException, InvalidUsernameException,
            UnauthorizedException, InvalidCustomerNameException,
            InvalidCustomerIdException, InvalidCustomerCardException {
        UUT.reset();
        createandlog(0); //Cashier

        //New customer
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);

        //New card
        String cardID = UUT.createCard();
        assertNotEquals("", cardID);

        //Attach card to customer
        assertTrue(UUT.attachCardToCustomer(cardID, customerID));

        //Check data
        Customer testCustomer = UUT.getCustomer(customerID);
        assertEquals(cardID, testCustomer.getCustomerCard());
    }

    @Test
    public void testAttachCardToCustomerUnauthorized(){
        UUT.reset();
        assertThrows(UnauthorizedException.class, () -> UUT.attachCardToCustomer("", 12));
    }

    @Test
    public void testAttachCardToCustomerInvalidID() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException,
            UnauthorizedException {
        UUT.reset();
        createandlog(0); //Cashier
        String cardID = UUT.createCard();
        assertNotEquals("", cardID);

        assertThrows(InvalidCustomerIdException.class, () -> UUT.attachCardToCustomer(cardID, 0));
        assertThrows(InvalidCustomerIdException.class, () -> UUT.attachCardToCustomer(cardID, -100));
        assertThrows(InvalidCustomerIdException.class, () -> UUT.attachCardToCustomer(cardID, null));
    }

    @Test
    public void testAttachCardToCustomerInvalidCardID() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, InvalidCustomerNameException,
            UnauthorizedException {
        UUT.reset();
        createandlog(0); //Cashier
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);

        assertThrows(InvalidCustomerCardException.class, () -> UUT.attachCardToCustomer("123", customerID));
        assertThrows(InvalidCustomerCardException.class, () -> UUT.attachCardToCustomer("", customerID));
        assertThrows(InvalidCustomerCardException.class, () -> UUT.attachCardToCustomer(null, customerID));
    }

    @Test
    public void testAttachCardToCustomerVariousErrors() throws InvalidPasswordException,
            InvalidRoleException, InvalidUsernameException,
            InvalidCustomerNameException, UnauthorizedException,
            InvalidCustomerIdException, InvalidCustomerCardException {
        UUT.reset();
        createandlog(0); //Cashier

        //New customer1
        int customerID = UUT.defineCustomer("Piero Rossi");
        assertNotEquals(-1, customerID);

        //New card1
        String cardID = UUT.createCard();
        assertNotEquals("", cardID);

        //Attach card to customer
        assertTrue(UUT.attachCardToCustomer(cardID, customerID));

        //Check data
        Customer testCustomer = UUT.getCustomer(customerID);
        assertEquals(cardID, testCustomer.getCustomerCard());

        //New customer 2
        int customerID2 = UUT.defineCustomer("Piero Rossino");
        assertNotEquals(-1, customerID2);

        //New card
        String cardID2 = UUT.createCard();
        assertNotEquals("", cardID2);

        //Attach card2 to customer2
        assertTrue(UUT.attachCardToCustomer(cardID2, customerID2));

        //Try to attach card2 to customer1, should return false since the card
        //has already a customer attached
        assertFalse(UUT.attachCardToCustomer(cardID2, customerID));

        //Check
        assertEquals(cardID, testCustomer.getCustomerCard());
    }
    // --------------- end attachCardToCustomer --------------- //

    // --------------- modifyPointsOnCard --------------- //
    @Test
    public void testModifyPointsOnCardNominalCase() throws InvalidPasswordException,
            InvalidRoleException, InvalidUsernameException,
            UnauthorizedException, InvalidCustomerCardException {
        UUT.reset();
        createandlog(0); //Cashier

        //New card
        String cardID = UUT.createCard();
        assertNotEquals("", cardID);

        //Attach card to customer
        assertTrue(UUT.modifyPointsOnCard(cardID, 12));
    }

    @Test
    public void testModifyPointsOnCardUnauthorized(){
        UUT.reset();
        assertThrows(UnauthorizedException.class, () -> UUT.modifyPointsOnCard("", 12));
    }

    @Test
    public void testModifyPointsOnCardInvalidCardID() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException, InvalidCustomerCardException {
        UUT.reset();
        createandlog(0); //Cashier

        assertThrows(InvalidCustomerCardException.class, () -> UUT.modifyPointsOnCard("123", 12));
        assertThrows(InvalidCustomerCardException.class, () -> UUT.modifyPointsOnCard("", 12));
        assertThrows(InvalidCustomerCardException.class, () -> UUT.modifyPointsOnCard(null, 12));
        assertFalse(UUT.modifyPointsOnCard("0000000002", 12));
    }

    @Test
    public void testModifyPointsOnCardNegativeBalance() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException, InvalidCustomerCardException {
        UUT.reset();
        createandlog(0); //Cashier

        //New card
        String cardID = UUT.createCard();
        assertNotEquals("", cardID);

        assertTrue(UUT.modifyPointsOnCard(cardID, 12));
        assertTrue(UUT.modifyPointsOnCard(cardID, -12));
        assertFalse(UUT.modifyPointsOnCard(cardID, -1));
    }
    // --------------- end modifyPointsOnCard --------------- //

}
