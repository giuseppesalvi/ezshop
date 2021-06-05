package it.polito.ezshop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.model.CreditCard;
import it.polito.ezshop.model.ProductTypeImpl;
import it.polito.ezshop.model.SaleTransactionImpl;
import it.polito.ezshop.model.TicketEntryImpl;

public class SaleTransactionImplTests {

	@Test
	public void testGettersSettersConstructors() {
		List<TicketEntry> products = new ArrayList<TicketEntry>();
		ProductTypeImpl p1 = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...", 100, "123-abc-123", false, 1);
		ProductTypeImpl p2 = new ProductTypeImpl("012345678990", "orange", 1.10, "");
		TicketEntryImpl t1 = new TicketEntryImpl(p1, 10);
		TicketEntryImpl t2 = new TicketEntryImpl(p2, 25, 0.5);		
		products.add(t1);
		products.add(t2);
		CreditCard c = new CreditCard("4485370086510891", 100.00);
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		LocalDate nowD = LocalDate.now();
		LocalTime nowT = LocalTime.now();
		
		sale1.setTicketNumber(14);
		assertEquals(sale1.getTicketNumber(), (Integer)14);
		sale1.setEntries(products);
		assertEquals(sale1.getEntries(), products);
		sale1.setDiscountRate(0.2);
		assertEquals((Double) sale1.getDiscountRate(), (Double)0.2);
		// (1.50 * 10 + 1.10 * 25 * 0.5 ) * 0.8 = 23
		assertEquals((Double) sale1.getPrice(), (Double) 23.00);
		sale1.setState("CLOSED");
		assertEquals(sale1.getState(), "CLOSED");
		sale1.setPaymentType("creditCard");
		assertEquals(sale1.getPaymentType(), "creditCard");
		sale1.setCreditCard(c);
		assertEquals(sale1.getCreditCard(), c);
		sale1.setDate(nowD);
		assertEquals(sale1.getDate(), nowD);
		sale1.setTime(nowT);
		assertEquals(sale1.getTime(), nowT);
		
		SaleTransactionImpl sale2 = new SaleTransactionImpl(12, products, 0.0, "OPEN", null, null, 0.0, null, null, null);
		sale2.setEntries(null);
		assertEquals(sale2.getEntries().size(), 0);

		
	}

	@Test
	public void testAddEntryWithNull() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		assertFalse(sale1.addEntry(null));
	}


	@Test
	public void testAddEntryWithValidInput() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		ProductTypeImpl p1 = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...");
		TicketEntryImpl t1 = new TicketEntryImpl(p1, 10, 0.5);
		assertTrue(sale1.addEntry(t1));
		assertEquals((Integer)sale1.getEntries().size(), (Integer)1);
	}
	
	@Test
	public void testDeleteEntryWithNull() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		assertNull(sale1.deleteEntry(null));

	}

	@Test
	public void testDeleteEntryWithProductNotPresent() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		assertNull(sale1.deleteEntry("012345678912"));

	}

	@Test
	public void testDeleteEntryWithProductPresent() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		ProductTypeImpl p1 = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...");
		TicketEntryImpl t1 = new TicketEntryImpl(p1, 10, 0.5);
		sale1.addEntry(t1);
		assertEquals(sale1.deleteEntry("012345678912"), t1);
	}

	@Test
	public void testDeleteEntryForLoop0Iterations() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		assertNull(sale1.deleteEntry("012345678912"));
	}


	@Test
	public void testDeleteEntryForLoop1Iterations() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		ProductTypeImpl p1 = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...");
		TicketEntryImpl t1 = new TicketEntryImpl(p1, 10, 0.5);
		sale1.addEntry(t1);
		assertEquals(sale1.deleteEntry("012345678912"), t1);
	}


	@Test
	public void testDeleteEntryForLooopMultipleIterations() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		ProductTypeImpl p1 = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...");
		ProductTypeImpl p2 = new ProductTypeImpl("0123456789128", "banana", 1.10, "banana notes...");
		TicketEntryImpl t1 = new TicketEntryImpl(p1, 10, 0.5);
		TicketEntryImpl t2 = new TicketEntryImpl(p2, 20, 0.1);
		sale1.addEntry(t1);
		sale1.addEntry(t2);
		assertEquals(sale1.deleteEntry("0123456789128"), t2);
	}


}
