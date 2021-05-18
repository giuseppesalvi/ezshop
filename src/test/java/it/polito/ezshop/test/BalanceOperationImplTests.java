package it.polito.ezshop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.time.LocalDate;

import org.junit.Test;

import it.polito.ezshop.model.BalanceOperationImpl;

public class BalanceOperationImplTests {
	
	// Black Box tests
	
	@Test
	public void testSetDateWithInvalidLocalDate() {
			BalanceOperationImpl obj = new BalanceOperationImpl((Integer)123, "2020-03-01", 10000.00, "ORDER");
			obj.setDate(null);
			assertEquals(obj.getDate(), LocalDate.parse("2020-03-01"));
	}

	@Test
	public void testSetDateWithValidLocalDate() {
		BalanceOperationImpl obj = new BalanceOperationImpl((Integer)123, "2020-03-01", 10000.00, "ORDER");
		obj.setDate(LocalDate.parse("2020-04-09"));
		assertEquals(obj.getDate(), LocalDate.parse("2020-04-09"));
	}

	/*
	@Test
	public void testSetMoneyWithInvalidMoney() {
		BalanceOperationImpl obj = new BalanceOperationImpl((Integer)123, "2020-03-01", 10000.00, "ORDER");
		obj.setMoney(null);
		assertEquals(obj.getMoney(), 10000.00);
	}
	*/

	@Test
	public void testSetMoneyWithValidMoney() {
		BalanceOperationImpl obj = new BalanceOperationImpl((Integer)123, "2020-03-01", 10000.00, "ORDER");
		obj.setMoney(5000.00);
		assertEquals((Double)obj.getMoney(), (Double)5000.00);
	}
	
	@Test
	public void testSetTypeWithNull() {
		BalanceOperationImpl obj = new BalanceOperationImpl((Integer)123, "2020-03-01", 10000.00, "ORDER");
		obj.setType(null);
		assertEquals(obj.getType(), "ORDER");
	}
	
	@Test
	public void testSetTypeWithSale() {
		BalanceOperationImpl obj = new BalanceOperationImpl((Integer)123, "2020-03-01", 10000.00, "ORDER");
		obj.setType("SALE");
		assertEquals(obj.getType(), "SALE");
	}

	@Test
	public void testSetTypeWithOrder() {
		BalanceOperationImpl obj = new BalanceOperationImpl((Integer)123, "2020-03-01", 10000.00, "SALE");
		obj.setType("ORDER");
		assertEquals(obj.getType(), "ORDER");
	}

	@Test
	public void testSetTypeWithReturn() {
		BalanceOperationImpl obj = new BalanceOperationImpl((Integer)123, "2020-03-01", 10000.00, "ORDER");
		obj.setType("RETURN");
		assertEquals(obj.getType(), "RETURN");
	}

	@Test
	public void testSetTypeWithCredit() {
		BalanceOperationImpl obj = new BalanceOperationImpl((Integer)123, "2020-03-01", 10000.00, "ORDER");
		obj.setType("CREDIT");
		assertEquals(obj.getType(), "CREDIT");
	}

	@Test
	public void testSetTypeWithDebit() {
		BalanceOperationImpl obj = new BalanceOperationImpl((Integer)123, "2020-03-01", 10000.00, "ORDER");
		obj.setType("DEBIT");
		assertEquals(obj.getType(), "DEBIT");
	}
	
	@Test
	public void testSetTypeWithNoMatch() {
		BalanceOperationImpl obj = new BalanceOperationImpl((Integer)123, "2020-03-01", 10000.00, "ORDER");
		obj.setType("test");
		assertEquals(obj.getType(), "ORDER");
	}

	@Test
	public void testSetBalanceIdWithNegativeNumber() {
		BalanceOperationImpl obj = new BalanceOperationImpl((Integer)123, "2020-03-01", 10000.00, "ORDER");
		obj.setBalanceId(-10);
		assertEquals(obj.getBalanceId(), 123);
	}

	
	@Test
	public void testSetBalanceIdWithAlreadyUsedId() {
		BalanceOperationImpl obj = new BalanceOperationImpl((Integer)123, "2020-03-01", 10000.00, "ORDER");
		BalanceOperationImpl obj2 = new BalanceOperationImpl((Integer)124, "2020-03-02", 300.00, "SALE");
		//EZShopMaps.loadMaps();
		//EZShopMaps.operations.put((Integer)123, obj);
		//EZShopMaps.operations.put((Integer)124, obj2);
		obj.setBalanceId(124);
		assertEquals(obj.getBalanceId(), 123);
	}
	
	
	@Test
	public void testSetBalanceIdWithValidId() {
		BalanceOperationImpl obj = new BalanceOperationImpl((Integer)123, "2020-03-01", 10000.00, "ORDER");
		BalanceOperationImpl obj2 = new BalanceOperationImpl((Integer)124, "2020-03-02", 300.00, "SALE");
		//EZShopMaps.loadMaps();
		//EZShopMaps.operations.put((Integer)123, obj);
		//EZShopMaps.operations.put((Integer)124, obj2);
		obj.setBalanceId(125);
		assertEquals(obj.getBalanceId(), 125);
	}
	
	@Test
	public void testConstructors() {
		BalanceOperationImpl obj = new BalanceOperationImpl((Integer)160, "2020-05-01", 200.00, "ORDER");
		BalanceOperationImpl obj2 = new BalanceOperationImpl(100.00, "SALE");
		BalanceOperationImpl obj3 = new BalanceOperationImpl(200.00, "SALE");
		assertNotEquals(obj.getBalanceId(), obj2.getBalanceId());
		assertNotEquals(obj2.getBalanceId(), obj3.getBalanceId());
	}
	

}
