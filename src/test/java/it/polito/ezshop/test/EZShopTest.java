package it.polito.ezshop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.exceptions.InvalidDiscountRateException;
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
		Integer product2Id = UUT.createProductType("orange", "0000000000512", 1.30, "");
		UUT.updateQuantity(product2Id, 5);
		assertTrue(UUT.addProductToSale(saleId, "0000000000512", 1));
		ProductType prod2 = UUT.getProductTypeByBarCode("0000000000512");
		assertEquals((Integer) prod2.getQuantity(), (Integer) 4);

		// add the same product twice
		assertTrue(UUT.addProductToSale(saleId, "012345678912", 2));
		assertEquals((Integer) prod.getQuantity(), (Integer) 1);
	}

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
	public void testApplyDiscountRateToProductTransactionIsNotOpen() throws InvalidUsernameException,
			InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductIdException,
			InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException,
			InvalidTransactionIdException, InvalidDiscountRateException, InvalidQuantityException, InvalidPaymentException {
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

		assertThrows(InvalidTransactionIdException.class,
				() -> UUT.applyDiscountRateToSale(null, 0.2));
		assertThrows(InvalidTransactionIdException.class,
				() -> UUT.applyDiscountRateToSale(-1, 0.2));
		assertThrows(InvalidTransactionIdException.class, () -> UUT.applyDiscountRateToProduct(0, "012345678912", 0.2));
	}
	
	@Test
	public void testApplyDiscountRateToSaleTransactionIsNotOpenOrClosed() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidQuantityException, InvalidPaymentException {
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
	public void testApplyDiscountRateToSaleNominalCase() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidQuantityException, InvalidPaymentException {
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
		assertEquals((Double)sale.getDiscountRate(), (Double)0.2);

		assertTrue(UUT.applyDiscountRateToSale(saleId, 0.8));
		assertEquals((Double)sale.getDiscountRate(), (Double)0.8);
		

	}

}
