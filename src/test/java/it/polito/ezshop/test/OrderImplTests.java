package it.polito.ezshop.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.model.OrderImpl;
import it.polito.ezshop.model.ProductTypeImpl;

public class OrderImplTests {

	@Test
	public void testGettersSettersConstructors() {
		ProductTypeImpl p = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...");
		OrderImpl o1 = new OrderImpl(p, 10, 1.30);
		OrderImpl o2 = new OrderImpl(p, 5, 1.10, "ISSUED", 11, 12);
		assertEquals((Integer)o2.getBalanceId(), (Integer)11);
		assertEquals(o2.getProductCode(), "012345678912");
		assertEquals((Double)o2.getPricePerUnit(), (Double)1.10);
		assertEquals((Integer)o2.getQuantity(),(Integer) 5);
		assertEquals(o2.getStatus(), "ISSUED");
		assertEquals((Integer)o2.getOrderId(), (Integer)12);
		assertEquals(o2.getProduct(), p);
		o1.setBalanceId(150);
		assertEquals((Integer)o1.getBalanceId(), (Integer)150);
		o1.setOrderId(120);
		assertEquals((Integer)o1.getOrderId(), (Integer)120);
		o1.setPricePerUnit(1.80);
		assertEquals((Double)o1.getPricePerUnit(), (Double)1.80);
		ProductTypeImpl p2 = new ProductTypeImpl("0123456789128", "banana", 1.80, "banana notes...");
		o1.setProduct(p2);
		assertEquals(o1.getProduct(), p2);
		o1.setProductCode("000000000012");
		assertEquals(o1.getProductCode(), "000000000012");
		o1.setQuantity(1);
		assertEquals((Integer)o1.getQuantity(),(Integer) 1);
		o1.setStatus("PAYED");
		assertEquals(o1.getStatus(), "PAYED");
	}
}
