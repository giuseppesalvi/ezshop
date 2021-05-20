package it.polito.ezshop.test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.*;
import org.junit.Before;
import org.junit.Test;
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
        assertNotEquals(productID, -1);
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(orderID, -1);
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
        assertEquals(orderID, -1);
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
        assertNotEquals(productID, -1);
        assertTrue(UUT.recordBalanceUpdate(1200.0));
        int orderID = UUT.payOrderFor("6291041500213", 12, 10.0);
        assertNotEquals(orderID, -1);
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
        assertEquals(orderID, -1);
    }

    @Test
    public void testPayOrderForBalanceNotEnough() throws InvalidPasswordException, InvalidRoleException,
            InvalidUsernameException, UnauthorizedException,
            InvalidProductDescriptionException, InvalidPricePerUnitException,
            InvalidProductCodeException, InvalidQuantityException{
        UUT.reset();
        createandlog(2); //Admin
        int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
        assertNotEquals(productID, -1);
        int orderID = UUT.payOrderFor("6291041500213", 12, 10.0);
        assertEquals(orderID, -1);
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
        assertNotEquals(productID, -1);
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(orderID, -1);
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
        assertNotEquals(productID, -1);
        assertTrue(UUT.recordBalanceUpdate(119.0));
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(orderID, -1);
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
        assertNotEquals(productID, -1);
        assertTrue(UUT.recordBalanceUpdate(120.0));
        int orderID = UUT.payOrderFor("6291041500213", 12, 10.0);
        assertNotEquals(orderID, -1);
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
        assertNotEquals(productID, -1);
        assertTrue(UUT.recordBalanceUpdate(240.0));
        int orderID = UUT.payOrderFor("6291041500213", 12, 10.0);
        assertNotEquals(orderID, -1);
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
        assertNotEquals(productID, -1);
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(orderID, -1);
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
        assertNotEquals(productID, -1);
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(orderID, -1);
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
        assertNotEquals(productID, -1);
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(orderID, -1);
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
        assertNotEquals(productID, -1);
        int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
        assertNotEquals(orderID, -1);
        assertTrue(UUT.recordBalanceUpdate(120.0));
        assertTrue(UUT.updatePosition(productID, "123-abc-123"));
        assertFalse(UUT.recordOrderArrival(orderID));
    }

}
