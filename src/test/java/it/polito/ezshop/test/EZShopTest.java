package it.polito.ezshop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPaymentException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class EZShopTest {

	EZShop UUT = new EZShop();

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
		Integer product2Id = UUT.createProductType("orange", "0000000000512", 1.30 , "");
		UUT.updateQuantity(product2Id, 5);
		assertTrue(UUT.addProductToSale(saleId, "0000000000512", 1));
		ProductType prod2 = UUT.getProductTypeByBarCode("0000000000512");
		assertEquals((Integer) prod2.getQuantity(), (Integer) 4);

		// add the same product twice
		assertTrue(UUT.addProductToSale(saleId, "012345678912", 2));
		assertEquals((Integer) prod.getQuantity(), (Integer) 1);
	}
}
