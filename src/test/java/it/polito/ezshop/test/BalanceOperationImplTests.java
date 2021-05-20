package it.polito.ezshop.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import it.polito.ezshop.model.BalanceOperationImpl;

public class BalanceOperationImplTests {
	
	@Test
	public void testGettersSettersConstructors() {

		BalanceOperationImpl op1 = new BalanceOperationImpl(100.00, "SALE");
		BalanceOperationImpl op2 = new BalanceOperationImpl(1, "2020-02-01", 140.00, "CREDIT");

		assertEquals((Double) 100.00 , (Double) op1.getMoney());
		assertEquals("SALE", op1.getType());
		assertEquals((Integer) 1, (Integer)op2.getBalanceId());
		assertEquals(LocalDate.parse("2020-02-01"), op2.getDate());
		
		op1.setBalanceId(2);
		assertEquals((Integer) 2, (Integer) op1.getBalanceId());
		op1.setDate(LocalDate.parse("2020-02-03"));
		assertEquals(LocalDate.parse("2020-02-03"), op1.getDate());
		op1.setMoney(120.00);
		assertEquals((Double) 120.00, (Double) op1.getMoney());
		op1.setType("CREDIT");
		assertEquals("CREDIT", op1.getType());


	}
}