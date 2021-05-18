package it.polito.ezshop.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.model.ProductTypeImpl;
import it.polito.ezshop.model.TicketEntryImpl;

public class TicketEntryImplTests {

		@Test
		public void testGettersSettersConstructors() {
			ProductTypeImpl p1 = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...", 100, "123-abc-123", false, 1);
			ProductTypeImpl p2 = new ProductTypeImpl("012345678990", "orange", 1.10, "");
			TicketEntryImpl t1 = new TicketEntryImpl(p1, 10);
			TicketEntryImpl t2 = new TicketEntryImpl(p1, 25, 0.5);		
			assertEquals(t1.getBarCode(), "012345678912");
			t1.setBarCode("0123456789128");
			assertEquals(t1.getBarCode(), "0123456789128");
			assertEquals(t1.getProductDescription(), "apple");
			t1.setProductDescription("banana");
			assertEquals(t1.getProductDescription(), "banana");
			assertEquals((Integer)t1.getAmount(), (Integer)10);
			t1.setAmount(30);
			assertEquals((Integer)t1.getAmount(), (Integer)30);
			assertEquals((Double)t1.getPricePerUnit(), (Double)1.50);
			t1.setPricePerUnit(2.20);
			assertEquals((Double)t1.getPricePerUnit(), (Double)2.20);
			assertEquals((Double)t2.getDiscountRate(), (Double)0.5);
			t1.setDiscountRate(0.2);
			assertEquals((Double)t1.getDiscountRate(), (Double)0.2);
			assertEquals(t1.getProduct(), p1);
			t1.setProduct(p2);
			assertEquals(t1.getProduct(), p2);
		}
}
