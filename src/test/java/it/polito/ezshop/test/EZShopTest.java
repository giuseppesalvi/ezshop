package it.polito.ezshop.test;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.junit.Assert.*;

public class EZShopTest {
	EZShopInterface UUT;

	@Before
	public void initialize() {
		UUT = new EZShop();
	}

	private void createandlog(int level)
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		String role;
		switch (level) {
		case 0:
			role = "Cashier";
			break;
		case 1:
			role = "ShopManager";
			break;
		default:
			role = "Administrator";
		}
		assertNotEquals((int) UUT.createUser(role, role, role), -1);
		assertNotNull(UUT.login(role, role));
	}

	// --------------- reset --------------- //
	@Test
	public void testResetNominalCase()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		assertEquals(UUT.getAllCustomers().size(), 0);
		assertEquals(UUT.getAllOrders().size(), 0);
		assertEquals(UUT.getAllProductTypes().size(), 0);
		assertEquals(UUT.getCreditsAndDebits(null, null).size(), 0);
		assertEquals((Double) UUT.computeBalance(), (Double) 0.);
		assertEquals(UUT.getAllUsers().size(), 1);
	}
	// --------------- end reset --------------- //

	// --------------- createUser--------------- //
	@Test
	public void testCreateUserWithInvalidUsername() {
		UUT.reset();

		assertThrows(InvalidUsernameException.class, () -> UUT.createUser(null, "admin", "Administrator"));
		assertThrows(InvalidUsernameException.class, () -> UUT.createUser("", "admin", "Administrator"));
	}

	@Test
	public void testCreateUserWithInvalidPassword() {
		UUT.reset();

		assertThrows(InvalidPasswordException.class, () -> UUT.createUser("admin", null, "Administrator"));
		assertThrows(InvalidPasswordException.class, () -> UUT.createUser("admin", "", "Administrator"));
	}

	@Test
	public void testCreateUserWithInvalidRole() {
		UUT.reset();

		assertThrows(InvalidRoleException.class, () -> UUT.createUser("admin", "admin", null));
		assertThrows(InvalidRoleException.class, () -> UUT.createUser("admin", "admin", ""));
		assertThrows(InvalidRoleException.class, () -> UUT.createUser("admin", "admin", "Admin"));
	}

	@Test
	public void testCreateUserWithUsernameAlreadyUsed()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin123", "Administrator");
		assertEquals(UUT.createUser("admin", "admin234", "Administrator"), (Integer) (-1));
	}

	@Test
	public void testCreateUserNominalCase()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();
		assertTrue(UUT.createUser("admin", "admin", "Administrator") > 0);
		assertTrue(UUT.createUser("cashier", "cashier", "Cashier") > 0);
		assertTrue(UUT.createUser("manager", "manager", "ShopManager") > 0);
	}
	// --------------- end createUser--------------- //

	// --------------- deleteUser --------------- //
	@Test
	public void testDeleteUserWithUnauthorizedUser()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.deleteUser(1));

		// cashier logged in
		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");
		assertThrows(UnauthorizedException.class, () -> UUT.deleteUser(1));

		// manager logged in
		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");
		assertThrows(UnauthorizedException.class, () -> UUT.deleteUser(1));
	}

	@Test
	public void testDeleteUserWithInvalidUserId()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		assertThrows(InvalidUserIdException.class, () -> UUT.deleteUser(0));
		assertThrows(InvalidUserIdException.class, () -> UUT.deleteUser(-1));
		assertThrows(InvalidUserIdException.class, () -> UUT.deleteUser(null));
	}

	@Test
	public void testDeleteUserWithNonExistingUser() throws InvalidUserIdException, UnauthorizedException,
			InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		assertFalse(UUT.deleteUser(100));
	}

	@Test
	public void testDeleteUserNominalCase() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, UnauthorizedException, InvalidUserIdException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		Integer cashier1Id = UUT.createUser("cashier1", "cashier", "Cashier");
		Integer admin1Id = UUT.createUser("admin1", "admin", "Administrator");
		Integer admin2Id = UUT.createUser("admin2", "admin", "Administrator");
		Integer manager1Id = UUT.createUser("manager1", "manager1", "ShopManager");

		assertEquals((Integer) UUT.getAllUsers().size(), (Integer) 5);

		assertTrue(UUT.deleteUser(cashier1Id));
		assertTrue(UUT.deleteUser(admin1Id));
		assertTrue(UUT.deleteUser(admin2Id));
		assertTrue(UUT.deleteUser(manager1Id));

		assertEquals((Integer) UUT.getAllUsers().size(), (Integer) 1);
	}
	// --------------- end deleteUser --------------- //

	// --------------- getAllUsers --------------- //
	@Test
	public void testGetAllUsersWithUnauthorizedUser()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.getAllUsers());

		// cashier logged in
		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");
		assertThrows(UnauthorizedException.class, () -> UUT.getAllUsers());

		// manager logged in
		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");
		assertThrows(UnauthorizedException.class, () -> UUT.getAllUsers());
	}

	@Test
	public void testGetAllUsersNominalCase() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, UnauthorizedException, InvalidUserIdException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		UUT.createUser("cashier1", "cashier", "Cashier");
		UUT.createUser("admin1", "admin", "Administrator");
		assertEquals((Integer) UUT.getAllUsers().size(), (Integer) 3);

		UUT.createUser("admin2", "admin", "Administrator");
		UUT.createUser("manager1", "manager1", "ShopManager");

		assertEquals((Integer) UUT.getAllUsers().size(), (Integer) 5);
	}
	// --------------- end getAllUsers --------------- //

	// --------------- getUser --------------- //
	@Test
	public void testGetUserWithUnauthorizedUser()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.getUser(1));

		// cashier logged in
		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");
		assertThrows(UnauthorizedException.class, () -> UUT.getUser(1));

		// manager logged in
		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");
		assertThrows(UnauthorizedException.class, () -> UUT.getUser(1));
	}

	@Test
	public void testGetUserWithInvalidUserId()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		assertThrows(InvalidUserIdException.class, () -> UUT.getUser(0));
		assertThrows(InvalidUserIdException.class, () -> UUT.getUser(-1));
		assertThrows(InvalidUserIdException.class, () -> UUT.getUser(null));
	}

	@Test
	public void testGetUserWithNonExistingUser() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, UnauthorizedException, InvalidUserIdException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		assertNull(UUT.getUser(100));
	}

	@Test
	public void testGetUserNominalCase() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, UnauthorizedException, InvalidUserIdException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		Integer cashier1Id = UUT.createUser("cashier1", "cashier", "Cashier");
		assertEquals(UUT.getUser(cashier1Id).getUsername(), "cashier1");
		assertEquals(UUT.getUser(cashier1Id).getPassword(), "cashier");
		assertEquals(UUT.getUser(cashier1Id).getRole(), "Cashier");
	}
	// --------------- end getUser --------------- //

	// --------------- updateUserRights --------------- //

	// --------------- getUser --------------- //
	@Test
	public void testUpdateUserRightsWithUnauthorizedUser()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.updateUserRights(100, "ShopManager"));

		// cashier logged in
		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");
		assertThrows(UnauthorizedException.class, () -> UUT.updateUserRights(100, "ShopManager"));

		// manager logged in
		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");
		assertThrows(UnauthorizedException.class, () -> UUT.updateUserRights(100, "ShopManager"));
	}

	@Test
	public void testUpdateUserRightsWithInvalidUserId()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		assertThrows(InvalidUserIdException.class, () -> UUT.updateUserRights(0, "Cashier"));
		assertThrows(InvalidUserIdException.class, () -> UUT.updateUserRights(-1, "Cashier"));
		assertThrows(InvalidUserIdException.class, () -> UUT.updateUserRights(null, "Cashier"));
	}

	@Test
	public void testUpdateUserRightsWithInvalidRole()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		Integer cashier1Id = UUT.createUser("cashier1", "cashier", "Cashier");
		assertThrows(InvalidRoleException.class, () -> UUT.updateUserRights(cashier1Id, ""));
		assertThrows(InvalidRoleException.class, () -> UUT.updateUserRights(cashier1Id, null));
		assertThrows(InvalidRoleException.class, () -> UUT.updateUserRights(cashier1Id, "adm"));
	}

	@Test
	public void testUpdateUserRightsWithNonExistingUser() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, UnauthorizedException, InvalidUserIdException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		assertFalse(UUT.updateUserRights(100, "ShopManager"));
	}

	@Test
	public void testUpdateUserRightsNominalCase() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, InvalidUserIdException, UnauthorizedException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		Integer cashier1Id = UUT.createUser("cashier1", "cashier", "Cashier");
		assertTrue(UUT.updateUserRights(cashier1Id, "ShopManager"));
	}
	// --------------- end updateUserRights --------------- //

	// --------------- login --------------- //
	@Test
	public void testLoginWithInvalidUsername() {
		UUT.reset();
		assertThrows(InvalidUsernameException.class, () -> UUT.login("", "abcde"));
		assertThrows(InvalidUsernameException.class, () -> UUT.login(null, "abcde"));
	}

	@Test
	public void testLoginWithInvalidPassword() {
		UUT.reset();
		assertThrows(InvalidPasswordException.class, () -> UUT.login("admin", ""));
		assertThrows(InvalidPasswordException.class, () -> UUT.login("admin", null));
	}

	@Test
	public void testLoginWithNonExistingUser() throws InvalidUsernameException, InvalidPasswordException {
		UUT.reset();
		assertNull(UUT.login("admin", "admin"));
	}

	@Test
	public void testLoginWithWrongCredentials()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		assertNull(UUT.login("admin", "admin12345"));
	}

	@Test
	public void testLoginNominalCase() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.createUser("manager", "manager", "ShopManager");
		assertNotNull(UUT.login("admin", "admin"));
		assertNotNull(UUT.login("manager", "manager"));
	}
	// --------------- end login --------------- //

	// --------------- logout --------------- //
	@Test
	public void testLogoutWithNoLoggedUser() {
		UUT.reset();
		assertFalse(UUT.logout());
	}

	@Test
	public void testLogoutNominalCase()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();
		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");
		assertTrue(UUT.logout());
	}
	// --------------- end logout --------------- //

	// --------------- issueOrder --------------- //
	@Test
	public void testIssueOrderNominalCase() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException {
		UUT.reset();
		createandlog(2); // Admin
		int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
		assertNotEquals(-1, productID);
		int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
		assertNotEquals(-1, orderID);
	}

	@Test
	public void testIssueOrderUnauthorized()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		// No user logged
		assertThrows(UnauthorizedException.class, () -> UUT.issueOrder("6291041500213", 12, 10.0));
		createandlog(0); // Cashier
		assertThrows(UnauthorizedException.class, () -> UUT.issueOrder("6291041500213", 12, 10.0));
	}

	@Test
	public void testIssueOrderInvalidProductCode()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		createandlog(1); // ShopManager
		assertThrows(InvalidProductCodeException.class, () -> UUT.issueOrder("6291041230213", 12, 10.0));
		assertThrows(InvalidProductCodeException.class, () -> UUT.issueOrder(null, 12, 10.0));
		assertThrows(InvalidProductCodeException.class, () -> UUT.issueOrder("", 12, 10.0));
	}

	@Test
	public void testIssueOrderInvalidQuantity()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		createandlog(1); // ShopManager
		assertThrows(InvalidQuantityException.class, () -> UUT.issueOrder("6291041230213", -12, 10.0));
		assertThrows(InvalidQuantityException.class, () -> UUT.issueOrder("6291041230213", 0, 10.0));
	}

	@Test
	public void testIssueOrderInvalidPricePerUnit()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		createandlog(1); // ShopManager
		assertThrows(InvalidPricePerUnitException.class, () -> UUT.issueOrder("6291041230213", 12, -10.0));
		assertThrows(InvalidPricePerUnitException.class, () -> UUT.issueOrder("6291041230213", 12, 0.0));
	}

	@Test
	public void testIssueOrderProductNotExists()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidQuantityException,
			UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException {
		UUT.reset();
		createandlog(1); // ShopManager
		int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
		assertEquals(-1, orderID);
	}
	// --------------- end issueOrder --------------- //

	// --------------- payOrderFor --------------- //
	@Test
	public void testPayOrderForNominalCase() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException {
		UUT.reset();
		createandlog(2); // Admin
		int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
		assertNotEquals(-1, productID);
		assertTrue(UUT.recordBalanceUpdate(1200.0));
		int orderID = UUT.payOrderFor("6291041500213", 12, 10.0);
		assertNotEquals(-1, orderID);
	}

	@Test
	public void testPayOrderForUnauthorized()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		// No user logged
		assertThrows(UnauthorizedException.class, () -> UUT.payOrderFor("6291041500213", 12, 10.0));
		createandlog(0); // Cashier
		assertThrows(UnauthorizedException.class, () -> UUT.payOrderFor("6291041500213", 12, 10.0));
	}

	@Test
	public void testPayOrderForInvalidProductCode()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		createandlog(1); // ShopManager
		assertThrows(InvalidProductCodeException.class, () -> UUT.payOrderFor("6291041230213", 12, 10.0));
		assertThrows(InvalidProductCodeException.class, () -> UUT.payOrderFor(null, 12, 10.0));
		assertThrows(InvalidProductCodeException.class, () -> UUT.payOrderFor("", 12, 10.0));
	}

	@Test
	public void testPayOrderForInvalidQuantity()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		createandlog(1); // ShopManager
		assertThrows(InvalidQuantityException.class, () -> UUT.payOrderFor("6291041230213", -12, 10.0));
		assertThrows(InvalidQuantityException.class, () -> UUT.payOrderFor("6291041230213", 0, 10.0));
	}

	@Test
	public void testPayOrderForInvalidPricePerUnit()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		createandlog(1); // ShopManager
		assertThrows(InvalidPricePerUnitException.class, () -> UUT.payOrderFor("6291041230213", 12, -10.0));
		assertThrows(InvalidPricePerUnitException.class, () -> UUT.payOrderFor("6291041230213", 12, 0.0));
	}

	@Test
	public void testPayOrderForProductNotExists()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidQuantityException,
			UnauthorizedException, InvalidPricePerUnitException, InvalidProductCodeException {
		UUT.reset();
		createandlog(1); // ShopManager
		int orderID = UUT.payOrderFor("6291041500213", 12, 10.0);
		assertEquals(-1, orderID);
	}

	@Test
	public void testPayOrderForBalanceNotEnough() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException {
		UUT.reset();
		createandlog(2); // Admin
		int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
		assertNotEquals(-1, productID);
		int orderID = UUT.payOrderFor("6291041500213", 12, 10.0);
		assertEquals(-1, orderID);
	}
	// --------------- end payOrderFor --------------- //

	// --------------- payOrder --------------- //
	@Test
	public void testPayOrderNominalCase()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException,
			InvalidQuantityException, InvalidOrderIdException {
		UUT.reset();
		createandlog(2); // Admin
		int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
		assertNotEquals(-1, productID);
		int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
		assertNotEquals(-1, orderID);
		assertTrue(UUT.recordBalanceUpdate(1200.0));
		assertTrue(UUT.payOrder(orderID));
		assertEquals(1080.0, UUT.computeBalance(), 0.001);
	}

	@Test
	public void testPayOrderUnauthorized()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		// No user logged
		assertThrows(UnauthorizedException.class, () -> UUT.payOrder(12));
		createandlog(0); // Cashier
		assertThrows(UnauthorizedException.class, () -> UUT.payOrder(12));
	}

	@Test
	public void testPayOrderNotExists() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException,
			UnauthorizedException, InvalidOrderIdException {
		UUT.reset();
		createandlog(2); // Admin
		assertFalse(UUT.payOrder(12));
	}

	@Test
	public void testPayOrderInvalidOrderID()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		createandlog(2); // Admin
		assertThrows(InvalidOrderIdException.class, () -> UUT.payOrder(null));
	}

	@Test
	public void testPayOrderBalanceNotEnough()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException,
			InvalidQuantityException, InvalidOrderIdException {
		UUT.reset();
		createandlog(2); // Admin
		int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
		assertNotEquals(-1, productID);
		assertTrue(UUT.recordBalanceUpdate(119.0));
		int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
		assertNotEquals(-1, orderID);
		assertFalse(UUT.payOrder(orderID));
	}

	@Test
	public void testPayOrderAlreadyReceived()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException,
			InvalidQuantityException, InvalidLocationException, InvalidProductIdException, InvalidOrderIdException {
		UUT.reset();
		createandlog(2); // Admin
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
	public void testPayOrderAlreadyPayed()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException,
			InvalidQuantityException, InvalidOrderIdException {
		UUT.reset();
		createandlog(2); // Admin
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
	public void testRecordOrderArrivalNominalCase()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException,
			InvalidQuantityException, InvalidOrderIdException, InvalidLocationException, InvalidProductIdException {
		UUT.reset();
		createandlog(2); // Admin
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
		assertEquals(12, (int) pr.getQuantity());
	}

	@Test
	public void testRecordOrderArrivalUnauthorized()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		// No user logged
		assertThrows(UnauthorizedException.class, () -> UUT.recordOrderArrival(12));
		createandlog(0); // Cashier
		assertThrows(UnauthorizedException.class, () -> UUT.recordOrderArrival(12));
	}

	@Test
	public void testRecordOrderArrivalInvalidOrderID() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, UnauthorizedException, InvalidOrderIdException, InvalidLocationException {
		UUT.reset();
		createandlog(2); // Admin
		assertThrows(InvalidOrderIdException.class, () -> UUT.recordOrderArrival(null));
		assertThrows(InvalidOrderIdException.class, () -> UUT.recordOrderArrival(0));
		assertFalse(UUT.recordOrderArrival(12));
	}

	@Test
	public void testRecordOrderArrivalLocationNotExists()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException,
			InvalidQuantityException, InvalidOrderIdException {
		UUT.reset();
		createandlog(2); // Admin
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
	public void testRecordOrderArrivalAlreadyArrived()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException,
			InvalidQuantityException, InvalidOrderIdException, InvalidLocationException, InvalidProductIdException {
		UUT.reset();
		createandlog(2); // Admin
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
		assertEquals(12, (int) pr.getQuantity());

		assertTrue(UUT.recordOrderArrival(orderID));
		assertEquals(12, (int) pr.getQuantity()); // Quantity does not change
	}

	@Test
	public void testRecordOrderArrivalNotPayed()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException,
			InvalidQuantityException, InvalidOrderIdException, InvalidLocationException, InvalidProductIdException {
		UUT.reset();
		createandlog(2); // Admin
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
			InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException {
		UUT.reset();
		createandlog(2); // Admin
		int productID = UUT.createProductType("Caffè", "6291041500213", 12.0, "Arabica");
		assertNotEquals(-1, productID);
		productID = UUT.createProductType("Apple", "563491053110", 2.0, "Machintosh");
		assertNotEquals(-1, productID);

		// Test with 0 orders
		List<Order> orders = UUT.getAllOrders();
		assertEquals(0, orders.size());

		int orderID = UUT.issueOrder("6291041500213", 12, 10.0);
		assertNotEquals(-1, orderID);
		orderID = UUT.issueOrder("563491053110", 33, 10.0);
		assertNotEquals(-1, orderID);

		// Test with 2 orders
		orders = UUT.getAllOrders();
		assertEquals(2, orders.size());
	}

	@Test
	public void testGetAllOrdersUnauthorized()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		// No user logged
		assertThrows(UnauthorizedException.class, () -> UUT.getAllOrders());
		createandlog(0); // Cashier
		assertThrows(UnauthorizedException.class, () -> UUT.getAllOrders());
	}
	// --------------- end getAllOrders --------------- //

	// --------------- defineCustomer --------------- //
	@Test
	public void testDefineCustomerNominalCase() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException {
		UUT.reset();
		createandlog(0); // Cashier
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);
	}

	@Test
	public void testDefineCustomerUnauthorized() {
		UUT.reset();
		assertThrows(UnauthorizedException.class, () -> UUT.defineCustomer("Piero Rossi"));
	}

	@Test
	public void testDefineCustomerInvalidName()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		createandlog(0); // Cashier
		assertThrows(InvalidCustomerNameException.class, () -> UUT.defineCustomer(""));
		assertThrows(InvalidCustomerNameException.class, () -> UUT.defineCustomer(null));
	}

	@Test
	public void testDefineCustomerAlreadyExists() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException {
		UUT.reset();
		createandlog(0); // Cashier
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);
		assertEquals(-1, (int) UUT.defineCustomer("Piero Rossi"));
	}
	// --------------- end defineCustomer --------------- //

	// --------------- modifyCustomer --------------- //
	@Test
	public void testModifyCustomerNominalCase() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException,
			InvalidCustomerCardException {
		UUT.reset();
		createandlog(0); // Cashier

		// New customer
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);

		// New card
		String cardID = UUT.createCard();
		assertNotEquals("", cardID);

		// Modifies data
		assertTrue(UUT.modifyCustomer(customerID, "Gianni Primo", cardID));

		// Check data
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
	public void testModifyCustomerInvalidName() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException {
		UUT.reset();
		createandlog(0); // Cashier

		// New customer
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);

		assertThrows(InvalidCustomerNameException.class, () -> UUT.modifyCustomer(customerID, "", ""));
		assertThrows(InvalidCustomerNameException.class, () -> UUT.modifyCustomer(customerID, null, ""));
	}

	@Test
	public void testModifyCustomerInvalidID()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		createandlog(0); // Cashier

		assertThrows(InvalidCustomerIdException.class, () -> UUT.modifyCustomer(0, "Gianni", ""));
		assertThrows(InvalidCustomerIdException.class, () -> UUT.modifyCustomer(null, "Gianni", ""));
	}

	@Test
	public void testModifyCustomerInvalidCard() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException {
		UUT.reset();
		createandlog(0); // Cashier

		// New customer
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);

		// Null and empty are allowed
		assertThrows(InvalidCustomerCardException.class, () -> UUT.modifyCustomer(customerID, "Piero Rossi", "0123"));

	}

	@Test
	public void testModifyCustomerCustomerNotExists() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException,
			InvalidCustomerCardException {
		UUT.reset();
		createandlog(0); // Cashier

		assertFalse(UUT.modifyCustomer(12, "Gianni pino", "0000000001"));
	}

	@Test
	public void testModifyCustomerModifyCard() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException,
			InvalidCustomerCardException {
		UUT.reset();
		createandlog(0); // Cashier

		// New customer
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);

		// New card
		String cardID = UUT.createCard();
		assertNotEquals("", cardID);

		// Attach card to customer
		assertTrue(UUT.modifyCustomer(customerID, "Gianni Primo", cardID));

		// Check data
		Customer testCustomer = UUT.getCustomer(customerID);
		assertEquals("Gianni Primo", testCustomer.getCustomerName());
		assertEquals(cardID, testCustomer.getCustomerCard());

		// Null as cardID, should remain the same
		assertTrue(UUT.modifyCustomer(customerID, "Gianni Primo", null));

		// Check
		assertEquals(cardID, testCustomer.getCustomerCard());

		// Empty as cardID, should detach the card
		assertTrue(UUT.modifyCustomer(customerID, "Gianni Primo", ""));

		// Check
		assertEquals("", testCustomer.getCustomerCard());

		// New customer 2
		int customerID2 = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID2);

		// New card
		String cardID2 = UUT.createCard();
		assertNotEquals("", cardID2);

		// Attach card2 to customer2
		assertTrue(UUT.modifyCustomer(customerID2, "Piero Rossi", cardID2));

		// Try to attach card2 to customer1, should return false since the card
		// has already a customer attached
		assertFalse(UUT.modifyCustomer(customerID, "Gianni Primo", cardID2));

		// Check
		assertEquals("", testCustomer.getCustomerCard());

		// Try to modify the name of customer1 with a name already present, should
		// return false
		assertFalse(UUT.modifyCustomer(customerID, "Piero Rossi", null));

		// Check
		assertEquals("Gianni Primo", testCustomer.getCustomerName());
	}
	// --------------- end modifyCustomer --------------- //

	// --------------- deleteCustomer --------------- //
	@Test
	public void testDeleteCustomerNominalCase() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException,
			InvalidCustomerCardException {
		UUT.reset();
		createandlog(0); // Cashier
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);

		String cardID = UUT.createCard();
		assertNotEquals("", cardID);

		assertTrue(UUT.attachCardToCustomer(cardID, customerID));

		// Delete
		assertTrue(UUT.deleteCustomer(customerID));

		// Check
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
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException {
		UUT.reset();
		createandlog(0); // Cashier
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);
		assertThrows(InvalidCustomerIdException.class, () -> UUT.deleteCustomer(0));
		assertThrows(InvalidCustomerIdException.class, () -> UUT.deleteCustomer(-100));
		assertThrows(InvalidCustomerIdException.class, () -> UUT.deleteCustomer(null));
	}

	@Test
	public void testDeleteCustomerCustomerNotExists() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
		UUT.reset();
		createandlog(0); // Cashier
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);
		assertFalse(UUT.deleteCustomer(customerID + 1));
	}
	// --------------- end deleteCustomer --------------- //

	// --------------- getCustomer --------------- //
	@Test
	public void testGetCustomerNominalCase() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
		UUT.reset();
		createandlog(0); // Cashier
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);

		assertNotEquals(-1, (int) UUT.defineCustomer("Paolo Rossi"));
		assertNotEquals(-1, (int) UUT.defineCustomer("Gianni Rossi"));

		Customer testCustomer = UUT.getCustomer(customerID);
		assertNotNull(testCustomer);

		// Check
		assertEquals("Piero Rossi", testCustomer.getCustomerName());
	}

	@Test
	public void testGetCustomerUnauthorized() {
		UUT.reset();
		assertThrows(UnauthorizedException.class, () -> UUT.getCustomer(12));
	}

	@Test
	public void testGetCustomerInvalidID() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException {
		UUT.reset();
		createandlog(0); // Cashier
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);
		assertThrows(InvalidCustomerIdException.class, () -> UUT.getCustomer(0));
		assertThrows(InvalidCustomerIdException.class, () -> UUT.getCustomer(-100));
		assertThrows(InvalidCustomerIdException.class, () -> UUT.getCustomer(null));
	}

	@Test
	public void testGetCustomerCustomerNotExists() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
		UUT.reset();
		createandlog(0); // Cashier
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);
		assertNull(UUT.getCustomer(customerID + 1));
	}
	// --------------- end getCustomer --------------- //

	// --------------- getAllCustomers --------------- //
	@Test
	public void testGetAllCustomersNominalCase() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException {
		UUT.reset();
		createandlog(0); // Cashier
		assertEquals(0, UUT.getAllCustomers().size());

		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);
		assertEquals(1, UUT.getAllCustomers().size());

		assertNotEquals(-1, (int) UUT.defineCustomer("Paolo Rossi"));
		assertNotEquals(-1, (int) UUT.defineCustomer("Gianni Rossi"));
		assertEquals(3, UUT.getAllCustomers().size());
	}

	@Test
	public void testGetAllCustomerUnauthorized() {
		UUT.reset();
		assertThrows(UnauthorizedException.class, () -> UUT.getAllCustomers());
	}
	// --------------- end getAllCustomers --------------- //

	// --------------- createCard --------------- //
	@Test
	public void testCreateCardNominalCase()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {
		UUT.reset();
		createandlog(0); // Cashier
		String cardID = UUT.createCard();
		assertNotNull(cardID);
		assertEquals(10, cardID.length());
	}

	@Test
	public void testCreateCardUnauthorized() {
		UUT.reset();
		assertThrows(UnauthorizedException.class, () -> UUT.createCard());
	}
	// --------------- end createCard --------------- //

	// --------------- attachCardToCustomer --------------- //
	@Test
	public void testAttachCardToCustomerNominalCase()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException,
			InvalidCustomerNameException, InvalidCustomerIdException, InvalidCustomerCardException {
		UUT.reset();
		createandlog(0); // Cashier

		// New customer
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);

		// New card
		String cardID = UUT.createCard();
		assertNotEquals("", cardID);

		// Attach card to customer
		assertTrue(UUT.attachCardToCustomer(cardID, customerID));

		// Check data
		Customer testCustomer = UUT.getCustomer(customerID);
		assertEquals(cardID, testCustomer.getCustomerCard());
	}

	@Test
	public void testAttachCardToCustomerUnauthorized() {
		UUT.reset();
		assertThrows(UnauthorizedException.class, () -> UUT.attachCardToCustomer("", 12));
	}

	@Test
	public void testAttachCardToCustomerInvalidID()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException {
		UUT.reset();
		createandlog(0); // Cashier
		String cardID = UUT.createCard();
		assertNotEquals("", cardID);

		assertThrows(InvalidCustomerIdException.class, () -> UUT.attachCardToCustomer(cardID, 0));
		assertThrows(InvalidCustomerIdException.class, () -> UUT.attachCardToCustomer(cardID, -100));
		assertThrows(InvalidCustomerIdException.class, () -> UUT.attachCardToCustomer(cardID, null));
	}

	@Test
	public void testAttachCardToCustomerInvalidCardID() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException {
		UUT.reset();
		createandlog(0); // Cashier
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);

		assertThrows(InvalidCustomerCardException.class, () -> UUT.attachCardToCustomer("123", customerID));
		assertThrows(InvalidCustomerCardException.class, () -> UUT.attachCardToCustomer("", customerID));
		assertThrows(InvalidCustomerCardException.class, () -> UUT.attachCardToCustomer(null, customerID));
	}

	@Test
	public void testAttachCardToCustomerVariousErrors() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException,
			InvalidCustomerCardException {
		UUT.reset();
		createandlog(0); // Cashier

		// New customer1
		int customerID = UUT.defineCustomer("Piero Rossi");
		assertNotEquals(-1, customerID);

		// New card1
		String cardID = UUT.createCard();
		assertNotEquals("", cardID);

		// Attach card to customer
		assertTrue(UUT.attachCardToCustomer(cardID, customerID));

		// Check data
		Customer testCustomer = UUT.getCustomer(customerID);
		assertEquals(cardID, testCustomer.getCustomerCard());

		// New customer 2
		int customerID2 = UUT.defineCustomer("Piero Rossino");
		assertNotEquals(-1, customerID2);

		// New card
		String cardID2 = UUT.createCard();
		assertNotEquals("", cardID2);

		// Attach card2 to customer2
		assertTrue(UUT.attachCardToCustomer(cardID2, customerID2));

		// Try to attach card2 to customer1, should return false since the card
		// has already a customer attached
		assertFalse(UUT.attachCardToCustomer(cardID2, customerID));

		// Check
		assertEquals(cardID, testCustomer.getCustomerCard());
	}
	// --------------- end attachCardToCustomer --------------- //

	// --------------- modifyPointsOnCard --------------- //
	@Test
	public void testModifyPointsOnCardNominalCase() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, UnauthorizedException, InvalidCustomerCardException {
		UUT.reset();
		createandlog(0); // Cashier

		// New card
		String cardID = UUT.createCard();
		assertNotEquals("", cardID);

		// Attach card to customer
		assertTrue(UUT.modifyPointsOnCard(cardID, 12));
	}

	@Test
	public void testModifyPointsOnCardUnauthorized() {
		UUT.reset();
		assertThrows(UnauthorizedException.class, () -> UUT.modifyPointsOnCard("", 12));
	}

	@Test
	public void testModifyPointsOnCardInvalidCardID() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, UnauthorizedException, InvalidCustomerCardException {
		UUT.reset();
		createandlog(0); // Cashier

		assertThrows(InvalidCustomerCardException.class, () -> UUT.modifyPointsOnCard("123", 12));
		assertThrows(InvalidCustomerCardException.class, () -> UUT.modifyPointsOnCard("", 12));
		assertThrows(InvalidCustomerCardException.class, () -> UUT.modifyPointsOnCard(null, 12));
		assertFalse(UUT.modifyPointsOnCard("0000000002", 12));
	}

	@Test
	public void testModifyPointsOnCardNegativeBalance() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, UnauthorizedException, InvalidCustomerCardException {
		UUT.reset();
		createandlog(0); // Cashier

		// New card
		String cardID = UUT.createCard();
		assertNotEquals("", cardID);

		assertTrue(UUT.modifyPointsOnCard(cardID, 12));
		assertTrue(UUT.modifyPointsOnCard(cardID, -12));
		assertFalse(UUT.modifyPointsOnCard(cardID, -1));
	}
	// --------------- end modifyPointsOnCard --------------- //

	// --------------- startSaleTransaction --------------- //
	@Test
	public void testStartSaleTransactionWithUnauthorizedUser()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.startSaleTransaction());
	}

	@Test
	public void testStartSaleTransactionNominalCase()
			throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		// administrator
		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");
		assertTrue(UUT.startSaleTransaction() >= 0);

		// cashier
		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");
		assertTrue(UUT.startSaleTransaction() >= 0);

		// manager
		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");
		assertTrue(UUT.startSaleTransaction() >= 0);
	}
	// --------------- endstartSaleTransaction --------------- //

	// --------------- addProductToSale --------------- //
	@Test
	public void testAddProductToSaleWithUnauthorizedUser() {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.addProductToSale(1, "012345678912", 1));

	}

	@Test
	public void testAddProductToSaleWithQuantityLessThanZero()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		// quantity less than 0
		assertThrows(InvalidQuantityException.class, () -> UUT.addProductToSale(1, "012345678912", -1));
		assertThrows(InvalidQuantityException.class, () -> UUT.addProductToSale(1, "012345678912", -100));
	}

	@Test
	public void testAddProductToSaleWithInvalidProductCode()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");

		// null product code
		assertThrows(InvalidProductCodeException.class, () -> UUT.addProductToSale(1, null, 10));

		// empty product code
		assertThrows(InvalidProductCodeException.class, () -> UUT.addProductToSale(1, "", 10));

		// invalid productCode
		assertThrows(InvalidProductCodeException.class, () -> UUT.addProductToSale(1, "012345678913", 10));
	}

	@Test
	public void testAddProductToSaleWithInvalidTransactionId()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		// null transaction id
		assertThrows(InvalidTransactionIdException.class, () -> UUT.addProductToSale(null, "012345678912", 10));

		// transaction id <= 0
		assertThrows(InvalidTransactionIdException.class, () -> UUT.addProductToSale(0, "012345678912", 10));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.addProductToSale(-1, "012345678912", 10));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.addProductToSale(-2, "012345678912", 10));
	}

	@Test
	public void testAddProductToSaleProductCodeDoesNotExist()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException,
			InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException, InvalidTransactionIdException, InvalidQuantityException, InvalidProductIdException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer saleId = UUT.startSaleTransaction();

		// empty list of products
		assertFalse(UUT.addProductToSale(saleId, "0123456789128", 10));

		// product not present
		Integer prodId = UUT.createProductType("apple", "012345678912", 1.10, "");
		assertFalse(UUT.addProductToSale(saleId, "0123456789128", 10));

		// product not present because deleted
		UUT.deleteProductType(prodId);
		assertFalse(UUT.addProductToSale(saleId, "012345678912", 10));
	}

	@Test
	public void testAddProductToSaleQuantityOfProductCannotSatisfyRequest()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException,
			InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException, InvalidTransactionIdException, InvalidQuantityException, InvalidProductIdException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer saleId = UUT.startSaleTransaction();
		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");

		// Quantity available is 0
		assertFalse(UUT.addProductToSale(saleId, "012345678912", 1));

		UUT.updateQuantity(productId, 10);

		// Quantity available is 10 < 100
		assertFalse(UUT.addProductToSale(saleId, "012345678912", 100));

		// Quantity available is 10 < 11
		assertFalse(UUT.addProductToSale(saleId, "012345678912", 11));
	}

	@Test
	public void testAddProductToSaleTransactionIdDoesNotIdentifyAnOpenTransaction()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException,
			InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException, InvalidTransactionIdException, InvalidQuantityException, InvalidProductIdException,
			InvalidPaymentException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);

		// list of sales is empty
		assertFalse(UUT.addProductToSale(1, "012345678912", 1));

		Integer saleId = UUT.startSaleTransaction();

		// sale with saleId is closed
		UUT.endSaleTransaction(saleId);
		assertFalse(UUT.addProductToSale(saleId, "012345678912", 1));

		// sale with saleId is payed
		UUT.receiveCashPayment(saleId, 100);
		assertFalse(UUT.addProductToSale(saleId, "012345678912", 1));
	}

	@Test
	public void testAddProductToSaleNominalScenario()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductIdException,
			UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, InvalidTransactionIdException, InvalidQuantityException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);

		Integer saleId = UUT.startSaleTransaction();

		// add the product to the sale
		assertTrue(UUT.addProductToSale(saleId, "012345678912", 7));
		ProductType prod = UUT.getProductTypeByBarCode("012345678912");
		assertEquals((Integer) prod.getQuantity(), (Integer) 3);

		// add another product to the same sale
		Integer product2Id = UUT.createProductType("orange", "0000000000512", 1.30, "");
		UUT.updateQuantity(product2Id, 5);
		assertTrue(UUT.addProductToSale(saleId, "0000000000512", 1));
		ProductType prod2 = UUT.getProductTypeByBarCode("0000000000512");
		assertEquals((Integer) prod2.getQuantity(), (Integer) 4);

		// add the same product twice
		assertTrue(UUT.addProductToSale(saleId, "012345678912", 2));
		assertEquals((Integer) prod.getQuantity(), (Integer) 1);
	}
	// --------------- end addProductToSale --------------- //

	// --------------- deleteProductFromSale --------------- //
	@Test
	public void testDeleteProductFromSaleWithUnauthorizedUser() {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.deleteProductFromSale(1, "012345678912", 1));

	}

	@Test

	public void testDeleteProductFromSaleWithQuantityLessThanZero()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		// quantity less than 0
		assertThrows(InvalidQuantityException.class, () -> UUT.deleteProductFromSale(1, "012345678912", -1));
		assertThrows(InvalidQuantityException.class, () -> UUT.deleteProductFromSale(1, "012345678912", -100));
	}

	@Test
	public void testDeleteProductFromSaleWithInvalidProductCode()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");

		// null product code
		assertThrows(InvalidProductCodeException.class, () -> UUT.deleteProductFromSale(1, null, 10));

		// empty product code
		assertThrows(InvalidProductCodeException.class, () -> UUT.deleteProductFromSale(1, "", 10));

		// invalid productCode
		assertThrows(InvalidProductCodeException.class, () -> UUT.deleteProductFromSale(1, "012345678913", 10));
	}

	@Test
	public void testDeleteProductFromSaleWithInvalidTransactionId()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		// null transaction id
		assertThrows(InvalidTransactionIdException.class, () -> UUT.deleteProductFromSale(null, "012345678912", 10));

		// transaction id <= 0
		assertThrows(InvalidTransactionIdException.class, () -> UUT.deleteProductFromSale(0, "012345678912", 10));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.deleteProductFromSale(-1, "012345678912", 10));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.deleteProductFromSale(-2, "012345678912", 10));
	}

	@Test
	public void testDeleteProductFromSaleProductCodeDoesNotExist()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException,
			InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException, InvalidTransactionIdException, InvalidQuantityException, InvalidProductIdException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer saleId = UUT.startSaleTransaction();

		// empty list of products
		assertFalse(UUT.deleteProductFromSale(saleId, "0123456789128", 10));

		// product not present
		UUT.createProductType("apple", "012345678912", 1.10, "");
		assertFalse(UUT.deleteProductFromSale(saleId, "0123456789128", 10));
	}

	@Test
	public void testDeleteProductFromSaleQuantityOfProductCannotSatisfyRequest()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException,
			InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException, InvalidTransactionIdException, InvalidQuantityException, InvalidProductIdException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer saleId = UUT.startSaleTransaction();
		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);

		// No products sold
		assertFalse(UUT.deleteProductFromSale(saleId, "012345678912", 1));

		// Quantity of product sold less than requested quantity 3 < 5
		UUT.addProductToSale(saleId, "012345678912", 3);
		assertFalse(UUT.deleteProductFromSale(saleId, "012345678912", 5));

	}

	@Test
	public void testDeleteProductFromSaleTransactionIdDoesNotIdentifyAnOpenTransaction()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException,
			InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException, InvalidTransactionIdException, InvalidQuantityException, InvalidProductIdException,
			InvalidPaymentException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);

		// list of sales is empty
		assertFalse(UUT.deleteProductFromSale(1, "012345678912", 1));

		Integer saleId = UUT.startSaleTransaction();

		UUT.addProductToSale(saleId, "012345678912", 5);

		// sale with saleId is closed
		UUT.endSaleTransaction(saleId);
		assertFalse(UUT.deleteProductFromSale(saleId, "012345678912", 1));

		// sale with saleId is payed
		UUT.receiveCashPayment(saleId, 100);
		assertFalse(UUT.deleteProductFromSale(saleId, "012345678912", 1));
	}

	@Test
	public void testDeleteProductFromSaleTransactionNominalCase()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException,
			InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			UnauthorizedException, InvalidProductIdException, InvalidTransactionIdException, InvalidQuantityException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId1 = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId1, 10);
		Integer productId2 = UUT.createProductType("orange", "0123456789128", 1.40, "");
		UUT.updateQuantity(productId2, 20);

		Integer saleId1 = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId1, "012345678912", 5);

		assertTrue(UUT.deleteProductFromSale(saleId1, "012345678912", 3));
		ProductType prod1 = UUT.getProductTypeByBarCode("012345678912");
		assertEquals(prod1.getQuantity(), (Integer) 8);

		assertTrue(UUT.deleteProductFromSale(saleId1, "012345678912", 1));
		assertEquals(prod1.getQuantity(), (Integer) 9);

		assertTrue(UUT.deleteProductFromSale(saleId1, "012345678912", 1));
		assertEquals(prod1.getQuantity(), (Integer) 10);

		Integer saleId2 = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId2, "012345678912", 8);
		UUT.addProductToSale(saleId2, "0123456789128", 10);
		assertTrue(UUT.deleteProductFromSale(saleId2, "0123456789128", 3));
	}
	// --------------- end deleteProductFromSale --------------- //

	// --------------- applyDisocuntRateToProduct --------------- //
	@Test
	public void testApplyDiscountRateToProductWithInvalidUser() {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.applyDiscountRateToProduct(1, "012345678912", 0.1));
	}

	@Test
	public void testAppyDiscountRateToProductWithInvalidDiscountRate()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");

		// Discount rate < 0
		assertThrows(InvalidDiscountRateException.class, () -> UUT.applyDiscountRateToProduct(1, "012345678912", -0.1));
		// Discount rate >= 1
		assertThrows(InvalidDiscountRateException.class, () -> UUT.applyDiscountRateToProduct(1, "012345678912", 1.1));
		assertThrows(InvalidDiscountRateException.class, () -> UUT.applyDiscountRateToProduct(1, "012345678912", 1.));
	}

	@Test
	public void testApplyDiscountRateToProductWithInvalidProductCode()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		assertThrows(InvalidProductCodeException.class, () -> UUT.applyDiscountRateToProduct(1, "", 0.2));
		assertThrows(InvalidProductCodeException.class, () -> UUT.applyDiscountRateToProduct(1, "012345678911", 0.2));
		assertThrows(InvalidProductCodeException.class, () -> UUT.applyDiscountRateToProduct(1, null, 0.2));
	}

	@Test
	public void testApplyDiscountRateToProductWithInvalidTransactionId()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		assertThrows(InvalidTransactionIdException.class,
				() -> UUT.applyDiscountRateToProduct(null, "012345678912", 0.2));
		assertThrows(InvalidTransactionIdException.class,
				() -> UUT.applyDiscountRateToProduct(-1, "012345678912", 0.2));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.applyDiscountRateToProduct(0, "012345678912", 0.2));
	}

	@Test
	public void testApplyDiscountRateToProductProductCodeDoesNotExist() throws InvalidUsernameException,
			InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductIdException,
			InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			InvalidTransactionIdException, InvalidDiscountRateException, InvalidQuantityException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer saleId1 = UUT.startSaleTransaction();

		// list of products is empty
		assertFalse(UUT.applyDiscountRateToProduct(saleId1, "012345678912", 0.2));

		Integer productId1 = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId1, 10);
		Integer productId2 = UUT.createProductType("orange", "0123456789128", 1.40, "");
		UUT.updateQuantity(productId2, 20);

		// list of products in the sale is empty
		assertFalse(UUT.applyDiscountRateToProduct(saleId1, "012345678912", 0.2));

		UUT.addProductToSale(saleId1, "012345678912", 5);

		// product not present in the sale
		assertFalse(UUT.applyDiscountRateToProduct(saleId1, "0123456789128", 0.2));
	}

	@Test
	public void testApplyDiscountRateToProductTransactionIsNotOpen()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException,
			InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, InvalidTransactionIdException, InvalidDiscountRateException,
			InvalidQuantityException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId1 = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId1, 10);
		Integer productId2 = UUT.createProductType("orange", "0123456789128", 1.40, "");
		UUT.updateQuantity(productId2, 20);

		// list of sales is empty
		assertFalse(UUT.applyDiscountRateToProduct(1, "012345678912", 0.2));

		Integer saleId1 = UUT.startSaleTransaction();
		Integer saleId2 = UUT.startSaleTransaction();

		UUT.addProductToSale(saleId1, "012345678912", 5);
		UUT.addProductToSale(saleId1, "0123456789128", 3);
		UUT.addProductToSale(saleId2, "012345678912", 1);

		// sale already closed
		UUT.endSaleTransaction(saleId1);
		assertFalse(UUT.applyDiscountRateToProduct(saleId1, "0123456789128", 0.2));

		// sale already payed
		UUT.receiveCashPayment(saleId1, 100);
		assertFalse(UUT.applyDiscountRateToProduct(saleId1, "0123456789128", 0.2));
	}

	@Test
	public void testApplyDiscountRateToProductNominalCase() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException,
			InvalidProductCodeException, InvalidPricePerUnitException, InvalidTransactionIdException,
			InvalidDiscountRateException, InvalidQuantityException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId1 = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId1, 10);
		Integer productId2 = UUT.createProductType("orange", "0123456789128", 1.40, "");
		UUT.updateQuantity(productId2, 20);

		Integer saleId1 = UUT.startSaleTransaction();
		Integer saleId2 = UUT.startSaleTransaction();

		UUT.addProductToSale(saleId1, "012345678912", 5);
		UUT.addProductToSale(saleId1, "0123456789128", 3);
		UUT.addProductToSale(saleId2, "012345678912", 1);

		assertTrue(UUT.applyDiscountRateToProduct(saleId1, "0123456789128", 0.2));
	}
	// --------------- end applyDisocuntRateToProduct --------------- //

	// --------------- applyDisocuntRateToSale --------------- //
	@Test
	public void testApplyDiscountRateToSaleWithInvalidUser() {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.applyDiscountRateToSale(1, 0.1));
	}

	@Test
	public void testAppyDiscountRateToSaleWithInvalidDiscountRate()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");

		// Discount rate < 0
		assertThrows(InvalidDiscountRateException.class, () -> UUT.applyDiscountRateToSale(1, -0.1));
		// Discount rate >= 1
		assertThrows(InvalidDiscountRateException.class, () -> UUT.applyDiscountRateToSale(1, 1.1));
		assertThrows(InvalidDiscountRateException.class, () -> UUT.applyDiscountRateToSale(1, 1.));
	}

	@Test
	public void testApplyDiscountRateToSaleWithInvalidTransactionId()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		assertThrows(InvalidTransactionIdException.class, () -> UUT.applyDiscountRateToSale(null, 0.2));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.applyDiscountRateToSale(-1, 0.2));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.applyDiscountRateToProduct(0, "012345678912", 0.2));
	}

	@Test
	public void testApplyDiscountRateToSaleTransactionIsNotOpenOrClosed() throws InvalidUsernameException,
			InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidDiscountRateException,
			UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, InvalidProductIdException, InvalidQuantityException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		// list of sales is empty
		assertFalse(UUT.applyDiscountRateToSale(1, 0.2));

		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);

		Integer saleId = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId, "012345678912", 5);
		UUT.endSaleTransaction(saleId);

		// sale with saleId is payed
		UUT.receiveCashPayment(saleId, 100);
		assertFalse(UUT.applyDiscountRateToSale(saleId, 0.2));
	}

	@Test
	public void testApplyDiscountRateToSaleNominalCase() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException,
			InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			InvalidProductIdException, InvalidQuantityException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);

		Integer saleId = UUT.startSaleTransaction();

		UUT.addProductToSale(saleId, "012345678912", 5);
		assertTrue(UUT.applyDiscountRateToSale(saleId, 0.1));

		UUT.endSaleTransaction(saleId);
		assertTrue(UUT.applyDiscountRateToSale(saleId, 0.2));

		SaleTransaction sale = UUT.getSaleTransaction(saleId);
		assertEquals((Double) sale.getDiscountRate(), (Double) 0.2);

		assertTrue(UUT.applyDiscountRateToSale(saleId, 0.8));
		assertEquals((Double) sale.getDiscountRate(), (Double) 0.8);
	}
	// --------------- end applyDisocuntRateToSale --------------- //

	// --------------- computePointsForSale --------------- //
	@Test
	public void testComputePointsForSaleWithUnauthorizedUser() {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.computePointsForSale(1));
	}

	@Test
	public void testComputePointsForSaleWithInvalidTransactionId()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		assertThrows(InvalidTransactionIdException.class, () -> UUT.computePointsForSale(null));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.computePointsForSale(0));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.computePointsForSale(-1));
	}

	@Test
	public void testComputePointsForSaleWithNonExistingTransaction() throws InvalidUsernameException,
			InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, UnauthorizedException {
		UUT.reset();

		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");

		assertEquals((Integer) UUT.computePointsForSale(1), (Integer) (-1));
	}

	@Test
	public void testComputePointsForSaleNominalCase() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, UnauthorizedException, InvalidProductIdException, InvalidProductDescriptionException,
			InvalidProductCodeException, InvalidPricePerUnitException, InvalidTransactionIdException,
			InvalidQuantityException, InvalidDiscountRateException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer saleId = UUT.startSaleTransaction();
		Integer productId1 = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId1, 10);
		Integer productId2 = UUT.createProductType("orange", "0123456789128", 1.40, "");
		UUT.updateQuantity(productId2, 20);

		assertEquals((Integer) UUT.computePointsForSale(saleId), (Integer) 0);

		UUT.addProductToSale(saleId, "012345678912", 7);
		int points = (int) (7 * 1.10 / 10);
		assertEquals((Integer) UUT.computePointsForSale(saleId), (Integer) points);

		UUT.addProductToSale(saleId, "0123456789128", 5);
		points = (int) ((7 * 1.10 + 5 * 1.40) / 10);
		assertEquals((Integer) UUT.computePointsForSale(saleId), (Integer) points);

		UUT.deleteProductFromSale(saleId, "012345678912", 3);
		points = (int) ((4 * 1.10 + 5 * 1.40) / 10);
		assertEquals((Integer) UUT.computePointsForSale(saleId), (Integer) points);

		UUT.addProductToSale(saleId, "0123456789128", 3);
		points = (int) ((4 * 1.10 + 8 * 1.40) / 10);
		assertEquals((Integer) UUT.computePointsForSale(saleId), (Integer) points);

		UUT.applyDiscountRateToSale(saleId, 0.3);
		points = (int) ((4 * 1.10 + 8 * 1.40) * (1 - 0.3) / 10);
		assertEquals((Integer) UUT.computePointsForSale(saleId), (Integer) points);

		UUT.applyDiscountRateToProduct(saleId, "012345678912", 0.5);
		points = (int) (((4 * 1.10) * (1 - 0.5) + 8 * 1.40) * (1 - 0.3) / 10);
		assertEquals((Integer) UUT.computePointsForSale(saleId), (Integer) points);

	}
	// --------------- end computePointsForSale --------------- //

	// --------------- endSaleTransaction --------------- //
	@Test
	public void testEndSaleTransactionWithUnauthorizedUser() {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.endSaleTransaction(1));
	}

	@Test
	public void testEndSaleTransactionWithInvalidTransactionId()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		assertThrows(InvalidTransactionIdException.class, () -> UUT.endSaleTransaction(null));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.endSaleTransaction(0));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.endSaleTransaction(-1));
	}

	@Test
	public void testEndSaleTransactionWithNotOpenTransaction()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException,
			InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");

		assertFalse(UUT.endSaleTransaction(1));

		Integer saleId = UUT.startSaleTransaction();
		UUT.endSaleTransaction(saleId);
		assertFalse(UUT.endSaleTransaction(saleId));
		UUT.receiveCashPayment(saleId, 100.00);
		assertFalse(UUT.endSaleTransaction(saleId));
	}

	@Test
	public void testEndSaleTransactionNominalCase() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, UnauthorizedException, InvalidTransactionIdException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer saleId = UUT.startSaleTransaction();
		assertTrue(UUT.endSaleTransaction(saleId));
	}
	// --------------- end endSaleTransaction --------------- //

	// --------------- deleteSaleTransaction --------------- //
	@Test
	public void testDeleteSaleTransactionWithUnauthorizedUser() {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.deleteSaleTransaction(1));
	}

	@Test
	public void testDeleteSaleTransactionWithInvalidTransactionId()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		assertThrows(InvalidTransactionIdException.class, () -> UUT.deleteSaleTransaction(null));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.deleteSaleTransaction(0));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.deleteSaleTransaction(-1));
	}

	@Test
	public void testDeleteSaleTransactionWithAlreadyPayedTransaction()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException,
			InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");

		assertFalse(UUT.deleteSaleTransaction(1));

		Integer saleId = UUT.startSaleTransaction();
		UUT.endSaleTransaction(saleId);
		UUT.receiveCashPayment(saleId, 100.00);
		assertFalse(UUT.deleteSaleTransaction(saleId));
	}

	@Test
	public void testDeleteSaleTransactionNominalCase()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException,
			InvalidTransactionIdException, InvalidProductDescriptionException, InvalidProductCodeException,
			InvalidPricePerUnitException, InvalidProductIdException, InvalidQuantityException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer saleId = UUT.startSaleTransaction();
		UUT.endSaleTransaction(saleId);
		assertTrue(UUT.deleteSaleTransaction(saleId));

		Integer saleId2 = UUT.startSaleTransaction();

		Integer productId1 = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId1, 10);
		Integer productId2 = UUT.createProductType("orange", "0123456789128", 1.40, "");
		UUT.updateQuantity(productId2, 20);

		UUT.addProductToSale(saleId2, "012345678912", 4);
		UUT.addProductToSale(saleId2, "0123456789128", 13);
		UUT.addProductToSale(saleId2, "012345678912", 3);
		UUT.deleteProductFromSale(saleId2, "0123456789128", 1);

		ProductType prod1 = UUT.getProductTypeByBarCode("012345678912");
		ProductType prod2 = UUT.getProductTypeByBarCode("0123456789128");

		assertEquals((Integer) prod1.getQuantity(), (Integer) (10 - 4 - 3));
		assertEquals((Integer) prod2.getQuantity(), (Integer) (20 - 13 + 1));

		UUT.endSaleTransaction(saleId2);
		assertTrue(UUT.deleteSaleTransaction(saleId2));
		assertEquals((Integer) prod1.getQuantity(), (Integer) (10));
		assertEquals((Integer) prod2.getQuantity(), (Integer) (20));
	}
	// --------------- end deleteSaleTransaction --------------- //

	// --------------- getSaleTransaction --------------- //
	@Test
	public void testGetSaleTransactionWithUnauthorizedUser() {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.getSaleTransaction(1));
	}

	@Test
	public void testGetSaleTransactionWithInvalidTransactionId()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		assertThrows(InvalidTransactionIdException.class, () -> UUT.getSaleTransaction(null));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.getSaleTransaction(0));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.getSaleTransaction(-1));
	}

	@Test
	public void testGetSaleTransactionWithOpenTransaction() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");

		assertNull(UUT.getSaleTransaction(1));

		Integer saleId = UUT.startSaleTransaction();
		assertNull(UUT.getSaleTransaction(saleId));
	}

	@Test
	public void testGetSaleTransactionNominalCase() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer saleId = UUT.startSaleTransaction();
		UUT.endSaleTransaction(saleId);
		assertNotNull(UUT.getSaleTransaction(saleId));
		UUT.receiveCashPayment(saleId, 100);
		assertNotNull(UUT.getSaleTransaction(saleId));
	}
	// --------------- end getSaleTransaction --------------- //

	// --------------- startReturnTransaction --------------- //
	@Test
	public void testStartReturnTransactionWithUnauthorizedUser() {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.startReturnTransaction(1));
	}

	@Test
	public void testStartReturnTransactionWithInvalidTransactionID()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		assertThrows(InvalidTransactionIdException.class, () -> UUT.startReturnTransaction(null));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.startReturnTransaction(0));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.startReturnTransaction(-1));

	}

	@Test
	public void testStartReturnTransactionWithTransactionNotAvailableAndPayed()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException,
			InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");

		// list of sales empty
		assertEquals(UUT.startReturnTransaction(1), (Integer) (-1));

		// sale was present but deleted
		Integer saleId = UUT.startSaleTransaction();
		UUT.endSaleTransaction(saleId);
		UUT.deleteSaleTransaction(saleId);
		assertEquals((Integer) (-1), UUT.startReturnTransaction(saleId));

		// sale transaction still open
		Integer saleId2 = UUT.startSaleTransaction();
		assertEquals((Integer) (-1), UUT.startReturnTransaction(saleId2));

		// sale transaction closed and not payed
		UUT.endSaleTransaction(saleId2);
		assertEquals((Integer) (-1), UUT.startReturnTransaction(saleId2));
	}

	@Test
	public void testStartReturnTransactionNominalCase() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, InvalidTransactionIdException, UnauthorizedException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer saleId = UUT.startSaleTransaction();
		UUT.endSaleTransaction(saleId);
		UUT.receiveCashPayment(saleId, 100);
		assertTrue(UUT.startReturnTransaction(saleId) >= 0);
	}
	// --------------- end startReturnTransaction --------------- //

	// --------------- returnProduct --------------- //
	@Test
	public void testReturnProductWithUnauthorizedUser() {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.returnProduct(1, "012345678912", 2));
	}

	@Test
	public void testReturnProductWithInvalidTransactionID()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		assertThrows(InvalidTransactionIdException.class, () -> UUT.returnProduct(null, "012345678912", 2));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.returnProduct(0, "012345678912", 2));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.returnProduct(-1, "012345678912", 2));
	}

	@Test
	public void testReturnProductWithInvalidQuantity()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");

		assertThrows(InvalidQuantityException.class, () -> UUT.returnProduct(1, "012345678912", 0));
		assertThrows(InvalidQuantityException.class, () -> UUT.returnProduct(1, "012345678912", -1));
	}

	@Test
	public void testReturnProductWithInvalidProductCode()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		assertThrows(InvalidProductCodeException.class, () -> UUT.returnProduct(1, null, 1));
		assertThrows(InvalidProductCodeException.class, () -> UUT.returnProduct(1, "", 1));
		assertThrows(InvalidProductCodeException.class, () -> UUT.returnProduct(1, "012345678911", 1));
	}

	@Test
	public void testReturnProductWhenTransactionDoesNotExist()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer prodId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(prodId, 10);

		// list of returns is empty
		assertFalse(UUT.returnProduct(1, "012345678912", 1));
	}

	@Test
	public void testReturnProductWhenProductToBeReturnedDoesNotExist() throws InvalidUsernameException,
			InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidProductCodeException,
			InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductIdException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer prodId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(prodId, 10);

		// product type not registered
		Integer saleId = UUT.startSaleTransaction();
		UUT.endSaleTransaction(saleId);
		UUT.receiveCashPayment(saleId, 100);

		Integer retId = UUT.startReturnTransaction(saleId);
		assertFalse(UUT.returnProduct(retId, "0123456789128", 1));
	}

	@Test
	public void testReturnProductWhenProductNotInTheSaleTransaction() throws InvalidUsernameException,
			InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidProductCodeException,
			InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductIdException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer prodId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(prodId, 10);
		Integer prodId2 = UUT.createProductType("apple", "0123456789128", 1.30, "");
		UUT.updateQuantity(prodId2, 20);

		Integer saleId = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId, "012345678912", 4);
		UUT.endSaleTransaction(saleId);
		UUT.receiveCashPayment(saleId, 100);

		// product not present in the sale transaction
		Integer retId = UUT.startReturnTransaction(saleId);
		assertFalse(UUT.returnProduct(retId, "0123456789128", 1));
	}

	@Test
	public void testReturnProductWhenAmountHigherThanSoldOne() throws InvalidUsernameException,
			InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidProductCodeException,
			InvalidQuantityException, UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductIdException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer prodId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(prodId, 10);
		Integer prodId2 = UUT.createProductType("apple", "0123456789128", 1.30, "");
		UUT.updateQuantity(prodId2, 20);

		Integer saleId = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId, "012345678912", 4);
		UUT.endSaleTransaction(saleId);
		UUT.receiveCashPayment(saleId, 100);

		// product present in the sale transaction but amount sold less than requested
		// one for return
		Integer retId = UUT.startReturnTransaction(saleId);
		assertFalse(UUT.returnProduct(retId, "012345678912", 5));
	}

	@Test
	public void testReturnProductNominalCase() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
			UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException,
			InvalidProductIdException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer prodId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(prodId, 10);
		Integer prodId2 = UUT.createProductType("apple", "0123456789128", 1.30, "");
		UUT.updateQuantity(prodId2, 20);

		Integer saleId = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId, "012345678912", 4);
		UUT.endSaleTransaction(saleId);
		UUT.receiveCashPayment(saleId, 100);

		Integer retId = UUT.startReturnTransaction(saleId);
		assertTrue(UUT.returnProduct(retId, "012345678912", 2));
	}
	// --------------- end returnProduct --------------- //

	// --------------- endReturnTransaction --------------- //
	@Test
	public void testEndReturnTransactionWithUnauthorizedUser() {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.endReturnTransaction(1, true));
	}

	@Test
	public void testEndReturnTransactionWithInvalidTransactionID()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");

		assertThrows(InvalidTransactionIdException.class, () -> UUT.endReturnTransaction(null, true));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.endReturnTransaction(0, true));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.endReturnTransaction(-1, true));
	}

	@Test
	public void testEndReturnTransactionWithNotOpenTransaction() throws InvalidUsernameException,
			InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, UnauthorizedException,
			InvalidProductIdException, InvalidProductCodeException, InvalidQuantityException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		// list of return empty
		assertFalse(UUT.endReturnTransaction(1, true));

		Integer prodId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(prodId, 10);
		Integer prodId2 = UUT.createProductType("apple", "0123456789128", 1.30, "");
		UUT.updateQuantity(prodId2, 20);

		Integer saleId = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId, "012345678912", 4);
		UUT.endSaleTransaction(saleId);
		UUT.receiveCashPayment(saleId, 100);

		// return transaction already closedreturnId
		Integer retId = UUT.startReturnTransaction(saleId);
		UUT.returnProduct(retId, "012345678912", 2);

		assertTrue(UUT.endReturnTransaction(retId, true));
		assertFalse(UUT.endReturnTransaction(retId, true));

		// return transaction already payed
		Integer retId2 = UUT.startReturnTransaction(saleId);
		UUT.returnProduct(retId2, "012345678912", 1);

		assertTrue(UUT.endReturnTransaction(retId2, true));
		UUT.returnCashPayment(retId2);
		assertFalse(UUT.endReturnTransaction(retId2, true));
	}

	@Test
	public void testEndReturnTransactionNominalCase() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, InvalidTransactionIdException, UnauthorizedException, InvalidProductIdException,
			InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer prodId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(prodId, 10);
		Integer prodId2 = UUT.createProductType("apple", "0123456789128", 1.30, "");
		UUT.updateQuantity(prodId2, 20);

		Integer saleId = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId, "012345678912", 4);
		UUT.addProductToSale(saleId, "0123456789128", 12);
		UUT.endSaleTransaction(saleId);
		UUT.receiveCashPayment(saleId, 100);

		Integer retId = UUT.startReturnTransaction(saleId);
		UUT.returnProduct(retId, "012345678912", 2);
		assertTrue(UUT.endReturnTransaction(retId, true));
		// check if quantity of products in the shelves is correct
		ProductType prod = UUT.getProductTypeByBarCode("012345678912");
		assertEquals(prod.getQuantity(), (Integer) (10 - 4 + 2));
		// check if sale price is correct
		SaleTransaction sale = UUT.getSaleTransaction(saleId);
		assertEquals((Double) (1.10 * (4 - 2) + 1.30 * (12)), (Double) sale.getPrice());

		Integer retId2 = UUT.startReturnTransaction(saleId);
		UUT.returnProduct(retId2, "0123456789128", 7);
		assertTrue(UUT.endReturnTransaction(retId2, false));
		// check if quantity of products in the shelves is correct
		ProductType prod2 = UUT.getProductTypeByBarCode("0123456789128");
		assertEquals(prod2.getQuantity(), (Integer) (20 - 12));
		// check if sale price is correct
		assertEquals((Double) (1.10 * (4 - 2) + 1.30 * (12)), (Double) sale.getPrice());
	}
	// --------------- end endReturnTransaction --------------- //

	// --------------- deleteReturnTransaction --------------- //
	@Test
	public void testDeleteReturnTransactionWithUnauthorizedUser() {
		UUT.reset();

		// no logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.deleteReturnTransaction(1));
	}

	@Test
	public void testDeleteReturnTransactionWithInvalidTransactionID()
			throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		UUT.reset();

		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");

		assertThrows(InvalidTransactionIdException.class, () -> UUT.deleteReturnTransaction(null));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.deleteReturnTransaction(0));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.deleteReturnTransaction(-1));
	}

	@Test
	public void testDeleteReturnTransactionWithNotExistingClosedReturnTransaction() throws InvalidUsernameException,
			InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, UnauthorizedException,
			InvalidProductIdException, InvalidProductCodeException, InvalidQuantityException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("admin", "admin", "Administrator");
		UUT.login("admin", "admin");

		// list of return empty, return transaction does not exist
		assertFalse(UUT.deleteReturnTransaction(1));

		Integer prodId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(prodId, 10);
		Integer prodId2 = UUT.createProductType("apple", "0123456789128", 1.30, "");
		UUT.updateQuantity(prodId2, 20);

		Integer saleId = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId, "012345678912", 4);
		UUT.endSaleTransaction(saleId);
		UUT.receiveCashPayment(saleId, 100);

		// return transaction still open
		Integer retId = UUT.startReturnTransaction(saleId);
		UUT.returnProduct(retId, "012345678912", 2);

		assertFalse(UUT.deleteReturnTransaction(retId));

		// return transaction already payed
		Integer retId2 = UUT.startReturnTransaction(saleId);
		UUT.returnProduct(retId2, "012345678912", 1);
		UUT.endReturnTransaction(retId2, true);
		UUT.returnCashPayment(retId2);
		assertFalse(UUT.deleteReturnTransaction(retId2));
	}

	@Test
	public void testDeleteReturnTransactionNominalCase() throws InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, InvalidTransactionIdException, UnauthorizedException, InvalidProductIdException,
			InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidPaymentException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer prodId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(prodId, 10);
		Integer prodId2 = UUT.createProductType("apple", "0123456789128", 1.30, "");
		UUT.updateQuantity(prodId2, 20);

		Integer saleId = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId, "012345678912", 4);
		UUT.addProductToSale(saleId, "0123456789128", 12);
		UUT.endSaleTransaction(saleId);
		UUT.receiveCashPayment(saleId, 100);

		Integer retId = UUT.startReturnTransaction(saleId);
		UUT.returnProduct(retId, "012345678912", 2);
		UUT.endReturnTransaction(retId, true);
		assertTrue(UUT.deleteReturnTransaction(retId));
		// check if quantity of products in the shelves is correct
		ProductType prod = UUT.getProductTypeByBarCode("012345678912");
		assertEquals(prod.getQuantity(), (Integer) (10 - 4));
		// check if sale price is correct
		SaleTransaction sale = UUT.getSaleTransaction(saleId);
		assertEquals((Double) (1.10 * (4) + 1.30 * (12)), (Double) sale.getPrice());

		Integer retId2 = UUT.startReturnTransaction(saleId);
		UUT.returnProduct(retId2, "0123456789128", 7);
		UUT.endReturnTransaction(retId2, false);
		assertTrue(UUT.deleteReturnTransaction(retId2));
		// check if quantity of products in the shelves is correct
		ProductType prod2 = UUT.getProductTypeByBarCode("0123456789128");
		assertEquals(prod2.getQuantity(), (Integer) (20 - 12));
		// check if sale price is correct
		assertEquals((Double) (1.10 * (4) + 1.30 * (12)), (Double) sale.getPrice());
	}
	// --------------- end deleteReturnTransaction --------------- //

	// --------------- receiveCashPayment --------------- //
	@Test
	public void testReceiveCashPaymentUnauthorizedUser()
			throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
		UUT.reset();
		assertThrows(UnauthorizedException.class, () -> UUT.receiveCashPayment(1, 100));
	}

	@Test
	public void testReceiveCashPaymentInvalidCashAmount()
			throws InvalidTransactionIdException, InvalidPasswordException, InvalidPaymentException,
			UnauthorizedException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");
		assertThrows(InvalidPaymentException.class, () -> UUT.receiveCashPayment(1, -200));
		assertThrows(InvalidPaymentException.class, () -> UUT.receiveCashPayment(1, 0));
	}

	@Test
	public void testReceiveCashPaymentInvalidTransactionId()
			throws InvalidTransactionIdException, InvalidPasswordException, InvalidPaymentException,
			UnauthorizedException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		assertThrows(InvalidTransactionIdException.class, () -> UUT.receiveCashPayment(0, 100));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.receiveCashPayment(null, 100));
		assertEquals(java.util.Optional.of(UUT.receiveCashPayment(1, 100)), java.util.Optional.of(-1.0));
	}

	@Test
	public void testReceiveCashPaymentWithNotOpenSaleTransaction() throws InvalidPasswordException,
			InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductIdException,
			InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException {

		UUT.reset();
		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);
		Integer product2Id = UUT.createProductType("orange", "0000000000512", 1.30, "");
		UUT.updateQuantity(product2Id, 5);

		Integer saleId = UUT.startSaleTransaction();

		// add the product to the sale
		UUT.addProductToSale(saleId, "012345678912", 7);
		UUT.addProductToSale(saleId, "0000000000512", 1);
		// pay and close the sale
		UUT.endSaleTransaction(saleId);
		UUT.receiveCashPayment(saleId, 100);
		// try to pay for the saleId that is in CLOSED state
		assertEquals(java.util.Optional.of(UUT.receiveCashPayment(saleId, 100)), java.util.Optional.of(-1.0));
	}

	@Test
	public void testReceiveCashPaymentNotEnoughCash() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, UnauthorizedException, InvalidProductIdException, InvalidQuantityException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidPaymentException {
		UUT.reset();
		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId = UUT.createProductType("apple", "012345678912", 2.00, "");
		UUT.updateQuantity(productId, 10);

		Integer saleId = UUT.startSaleTransaction();
		// add the product to the sale
		UUT.addProductToSale(saleId, "012345678912", 10);
		// try to pay with the amount of cash that is not enough
		UUT.endSaleTransaction(saleId);
		assertEquals(java.util.Optional.of(UUT.receiveCashPayment(saleId, 10)), java.util.Optional.of(-1.0));

	}

	@Test
	public void testReceiveCashPaymentNominalCase() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, UnauthorizedException, InvalidProductIdException, InvalidQuantityException,
			InvalidTransactionIdException, InvalidProductCodeException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidPaymentException {

		UUT.reset();
		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);
		Integer product2Id = UUT.createProductType("orange", "0000000000512", 1.30, "");
		UUT.updateQuantity(product2Id, 5);

		Integer saleId = UUT.startSaleTransaction();

		// add the product to the sale
		UUT.addProductToSale(saleId, "012345678912", 7);
		UUT.addProductToSale(saleId, "0000000000512", 1);

		UUT.endSaleTransaction(saleId);
		assertEquals(java.util.Optional.of(UUT.receiveCashPayment(saleId, 100)), java.util.Optional.of(91.0));
		SaleTransaction sale = UUT.getSaleTransaction(saleId);
	}

	// --------------- end receiveCashPayment --------------- //

	// --------------- receiveCreditCardPayment --------------- //

	@Test
	public void testReceiveCreditCardPaymentUnauthenticatedUser() throws UnauthorizedException {
		UUT.reset();
		assertThrows(UnauthorizedException.class, () -> UUT.receiveCreditCardPayment(1, "4485370086510891"));
	}

	@Test
	public void testReceiveCreditCardPaymentInvalidCard() throws InvalidCreditCardException, InvalidPasswordException,
			InvalidRoleException, InvalidUsernameException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		assertThrows(InvalidCreditCardException.class, () -> UUT.receiveCreditCardPayment(1, null));
		assertThrows(InvalidCreditCardException.class, () -> UUT.receiveCreditCardPayment(1, ""));
		assertThrows(InvalidCreditCardException.class, () -> UUT.receiveCreditCardPayment(1, "123"));
	}

	@Test
	public void testReceiveCreditCardPaymentInvalidSaleId() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidCreditCardException, InvalidTransactionIdException, UnauthorizedException {

		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");
		// saleId is not valid
		assertThrows(InvalidTransactionIdException.class, () -> UUT.receiveCreditCardPayment(0, "4485370086510891"));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.receiveCreditCardPayment(null, "4485370086510891"));
		// saleId does not exist
		assertFalse(UUT.receiveCreditCardPayment(1, "4485370086510891"));
	}

	@Test
	public void testReceiveCreditCardPaymentWithNotRegisteredCreditCard()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidCreditCardException,
			InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException {

		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);

		Integer saleId = UUT.startSaleTransaction();

		// add the product to the sale
		UUT.addProductToSale(saleId, "012345678912", 10);
		// pay and close the sale
		UUT.endSaleTransaction(saleId);
		// credit card is valid but not registered
		assertFalse(UUT.receiveCreditCardPayment(saleId, "5352937369048372"));
	}

	@Test
	public void testReceiveCreditCardPaymentWithNotOpenSaleTransaction()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidCreditCardException,
			InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException {

		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);

		Integer saleId = UUT.startSaleTransaction();

		// add the product to the sale
		UUT.addProductToSale(saleId, "012345678912", 10);
		// pay and close the sale
		UUT.endSaleTransaction(saleId);
		UUT.receiveCreditCardPayment(saleId, "4485370086510891");
		// try to pay for the saleId that is in CLOSED state
		assertFalse(UUT.receiveCreditCardPayment(saleId, "4485370086510891"));
	}

	@Test
	public void testReceiveCreditCardPaymentWithNotEnoughMoneyInCard()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidCreditCardException,
			InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException {

		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);

		Integer saleId = UUT.startSaleTransaction();

		// add the product to the sale
		UUT.addProductToSale(saleId, "012345678912", 10);
		// close the sale
		UUT.endSaleTransaction(saleId);
		// credit card is valid does not have enough money
		assertFalse(UUT.receiveCreditCardPayment(saleId, "4716258050958645"));
	}

	@Test
	public void testReceiveCreditCardPaymentNominalCase() throws UnauthorizedException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException,
			InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidProductIdException,
			InvalidQuantityException, InvalidTransactionIdException, InvalidCreditCardException {

		UUT.reset();
		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);

		Integer saleId = UUT.startSaleTransaction();

		// add the product to the sale
		UUT.addProductToSale(saleId, "012345678912", 7);
		UUT.endSaleTransaction(saleId);
		assertTrue(UUT.receiveCreditCardPayment(saleId, "4485370086510891"));
	}

	// --------------- end receiveCreditCardPayment --------------- //

	// --------------- returnCashPayment --------------- //
	@Test
	public void testReturnCashPaymentUnauthenticatedUser() throws UnauthorizedException {
		UUT.reset();
		assertThrows(UnauthorizedException.class, () -> UUT.returnCashPayment(1));
	}

	@Test
	public void testReturnCashPaymentInvalidReturnId() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		assertThrows(InvalidTransactionIdException.class, () -> UUT.returnCashPayment(0));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.returnCashPayment(-1));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.returnCashPayment(null));
		// return -1 if the returnId is valid but not in the db
		assertTrue(UUT.returnCashPayment(1) == -1);
	}

	@Test
	public void testReturnCashPaymentWithNotOpenReturnTransaction() throws InvalidPasswordException,
			InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException,
			InvalidTransactionIdException, InvalidPaymentException, InvalidQuantityException {

		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");
		// add product
		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);
		// complete a sale
		Integer saleId = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId, "012345678912", 7);
		UUT.endSaleTransaction(saleId);
		UUT.receiveCashPayment(saleId, 100);
		// start return transaction
		Integer retId = UUT.startReturnTransaction(saleId);
		UUT.returnProduct(retId, "012345678912", 7);
		// return transaction should be in CLOSED state before return cash
		assertTrue(UUT.returnCashPayment(retId) == -1);
		UUT.endReturnTransaction(retId, true);
		UUT.returnCashPayment(retId);
		// cannot return cash if the Transaction is not in CLOSED state
		assertTrue(UUT.returnCashPayment(retId) == -1);

	}

	@Test
	public void testReturnCashPaymentNominalCase() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, UnauthorizedException, InvalidTransactionIdException, InvalidPaymentException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException,
			InvalidProductIdException, InvalidQuantityException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");
		// add product
		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);
		// complete a sale
		Integer saleId = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId, "012345678912", 7);
		UUT.endSaleTransaction(saleId);
		UUT.receiveCashPayment(saleId, 100);
		// start return transaction
		Integer retId = UUT.startReturnTransaction(saleId);
		UUT.returnProduct(retId, "012345678912", 7);
		UUT.endReturnTransaction(retId, true);
		// amount payed == return cash amount
		assertTrue(UUT.returnCashPayment(retId) - 7.70 < 0.000001d);
	}
	// --------------- end returnCashPayment --------------- //

	// --------------- returnCreditCardPayment --------------- //
	@Test
	public void testReturnCreditCardPaymentUnauthenticatedUser() throws UnauthorizedException {
		UUT.reset();
		assertThrows(UnauthorizedException.class, () -> UUT.returnCreditCardPayment(1, "123"));
	}

	@Test
	public void testReturnCreditCardPaymentInvalidReturnId() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException, InvalidCreditCardException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		assertThrows(InvalidTransactionIdException.class, () -> UUT.returnCreditCardPayment(0, "4485370086510891"));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.returnCreditCardPayment(-1, "4485370086510891"));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.returnCreditCardPayment(null, "4485370086510891"));
		// return -1 if the returnId is valid but not in the db
		assertTrue(UUT.returnCreditCardPayment(1, "4485370086510891") == -1);
	}

	@Test
	public void testReturnCreditCardPaymentWithInvalidCreditCard()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		assertThrows(InvalidCreditCardException.class, () -> UUT.returnCreditCardPayment(1, null));
		assertThrows(InvalidCreditCardException.class, () -> UUT.returnCreditCardPayment(1, ""));
		assertThrows(InvalidCreditCardException.class, () -> UUT.returnCreditCardPayment(1, "123"));
	}

	@Test
	public void testReturnCreditCardPaymentWithNotOpenReturnTransaction()
			throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException,
			InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException,
			InvalidProductIdException, InvalidTransactionIdException, InvalidPaymentException, InvalidQuantityException,
			InvalidCreditCardException {

		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");
		// add product
		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);
		// complete a sale
		Integer saleId = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId, "012345678912", 7);
		UUT.endSaleTransaction(saleId);
		UUT.receiveCashPayment(saleId, 100);
		// start return transaction
		Integer retId = UUT.startReturnTransaction(saleId);
		UUT.returnProduct(retId, "012345678912", 7);
		// return transaction should be in CLOSED state before return cash
		assertTrue(UUT.returnCashPayment(retId) == -1);
		UUT.endReturnTransaction(retId, true);
		UUT.returnCreditCardPayment(retId, "4485370086510891");
		// cannot return cash if the Transaction is not in CLOSED state
		assertTrue(UUT.returnCreditCardPayment(retId, "4485370086510891") == -1);
	}

	@Test
	public void testReturnCreditCardPaymentWithNotRegisteredCreditCard() throws InvalidPasswordException,
			InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductIdException,
			InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException,
			InvalidCreditCardException, InvalidProductDescriptionException, InvalidPricePerUnitException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);
		// add a complete sale
		Integer saleId = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId, "012345678912", 7);
		UUT.endSaleTransaction(saleId);
		UUT.receiveCreditCardPayment(saleId, "4485370086510891");
		// start return transaction
		Integer retId = UUT.startReturnTransaction(saleId);
		UUT.returnProduct(retId, "012345678912", 7);
		UUT.endReturnTransaction(retId, true);
		// credit card is valid but not registered
		assertTrue(UUT.returnCreditCardPayment(retId, "5352937369048372") == -1.0);

	}

	@Test
	public void testReturnCreditCardPaymentNominalCase() throws InvalidPasswordException, InvalidRoleException,
			InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException,
			InvalidPricePerUnitException, InvalidProductCodeException, InvalidQuantityException,
			InvalidTransactionIdException, InvalidProductIdException, InvalidCreditCardException {

		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		Integer productId = UUT.createProductType("apple", "012345678912", 1.10, "");
		UUT.updateQuantity(productId, 10);
		// add a complete sale
		Integer saleId = UUT.startSaleTransaction();
		UUT.addProductToSale(saleId, "012345678912", 7);
		UUT.endSaleTransaction(saleId);
		UUT.receiveCreditCardPayment(saleId, "4485370086510891");
		// start return transaction
		Integer retId = UUT.startReturnTransaction(saleId);
		UUT.returnProduct(retId, "012345678912", 7);
		UUT.endReturnTransaction(retId, true);
		// amount payed == return cash amount
		assertTrue(UUT.returnCreditCardPayment(retId, "4485370086510891") - 7.70 < 0.000001d);
	}

	// --------------- end returnCreditCardPayment --------------- //
	// --------------- recordBalanceUpdate --------------- //
	@Test
	public void testRecordBalanceUpdateUnauthorizedUser()
			throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();
		// not Logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.recordBalanceUpdate(100));
		// Logged in but the user is cashier
		UUT.createUser("cashier", "cashier", "Cashier");
		UUT.login("cashier", "cashier");
		assertThrows(UnauthorizedException.class, () -> UUT.recordBalanceUpdate(100));
	}

	@Test
	public void testRecordBalanceUpdateInvalidAmountToBeAdded()
			throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");
		assertTrue(UUT.recordBalanceUpdate(100));
		// cannot reduce the balance when the Balance + toBeAdded < 0
		assertFalse(UUT.recordBalanceUpdate(-200));
	}

	@Test
	public void testRecordBalanceUpdateNominalCase()
			throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		// add positive amount
		assertTrue(UUT.recordBalanceUpdate(100));
		// add negative amount
		assertTrue(UUT.recordBalanceUpdate(-100));
	}
	// --------------- end recordBalanceUpdate --------------- //

	// --------------- getCreditsAndDebits --------------- //
	@Test
	public void testGetCreditsAndDebitsUnauthorizedUser() throws UnauthorizedException {
		UUT.reset();
		// not Logged in user
		assertThrows(UnauthorizedException.class, () -> UUT.getCreditsAndDebits(null, null));
	}

	@Test
	public void testGetCreditsAndDebitsWithNullDates()
			throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		UUT.recordBalanceUpdate(100);
		List<BalanceOperation> balanceOperations = UUT.getCreditsAndDebits(null, null);
		assertTrue(balanceOperations.get(0).getMoney() - 100 == 0);
	}

	@Test
	public void testGetCreditsAndDebitsReversedDate()
			throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		UUT.recordBalanceUpdate(100);
		LocalDate tomorrow = LocalDate.now().plus(Period.ofDays(1));
		LocalDate yesterday = LocalDate.now().minus(Period.ofDays(1));
		List<BalanceOperation> balanceOperations = UUT.getCreditsAndDebits(tomorrow, yesterday);
		assertTrue(balanceOperations.get(0).getMoney() - 100 == 0);
	}

	@Test
	public void testGetCreditsAndDebitsNominalCase()
			throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		UUT.recordBalanceUpdate(100);
		LocalDate tomorrow = LocalDate.now().plus(Period.ofDays(1));
		LocalDate yesterday = LocalDate.now().minus(Period.ofDays(1));
		List<BalanceOperation> balanceOperations = UUT.getCreditsAndDebits(yesterday, tomorrow);
		assertTrue(balanceOperations.get(0).getMoney() - 100 == 0);
	}
	// --------------- end getCreditsAndDebits --------------- //

	// --------------- computeBalance --------------- //
	@Test
	public void testComputeBalanceUnauthorizedUser() throws UnauthorizedException {
		UUT.reset();

		assertThrows(UnauthorizedException.class, () -> UUT.computeBalance());
	}

	@Test
	public void testComputeBalanceNominalCase()
			throws UnauthorizedException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException {
		UUT.reset();

		UUT.createUser("manager", "manager", "ShopManager");
		UUT.login("manager", "manager");

		UUT.recordBalanceUpdate(100);
		assertTrue(UUT.computeBalance() - 100.0 == 0);
	}
	// --------------- end computeBalance --------------- //

}