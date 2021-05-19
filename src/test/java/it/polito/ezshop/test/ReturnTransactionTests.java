package it.polito.ezshop.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.model.ProductTypeImpl;
import it.polito.ezshop.model.ReturnTransaction;
import it.polito.ezshop.model.SaleTransactionImpl;
import it.polito.ezshop.model.TicketEntryImpl;

public class ReturnTransactionTests {

	@Test
	public void testGettersSettersConstructors() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		SaleTransactionImpl sale2 = new SaleTransactionImpl();
		List<TicketEntryImpl> products = new ArrayList<TicketEntryImpl>();
		ProductTypeImpl p1 = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...", 100, "123-abc-123", false, 1);
		ProductTypeImpl p2 = new ProductTypeImpl("012345678990", "orange", 1.10, "");
		TicketEntryImpl t1 = new TicketEntryImpl(p1, 10);
		TicketEntryImpl t2 = new TicketEntryImpl(p2, 25, 0.5);		
		products.add(t1);
		products.add(t2);
		ReturnTransaction ret1 = new ReturnTransaction(sale1);
		ReturnTransaction ret2 = new ReturnTransaction(10, sale1, products, "OPEN", false, 0.0);
		assertEquals((Integer)ret2.getReturnID(), (Integer)10);
		assertEquals(ret1.getTransaction(), sale1);
		ret1.setTransaction(sale2);
		assertEquals(ret1.getTransaction(), sale2);	
		assertEquals(ret2.getProducts(), products);
		ret1.setProducts(products);
		assertEquals(ret1.getProducts(), products);
		ret2.setProducts(null);
		assertEquals(ret2.getProducts().size(),0);
		assertEquals(ret2.getState(), "OPEN");
		ret2.setState("CLOSED");
		assertEquals(ret2.getState(), "CLOSED");
		assertEquals(ret2.isCommit(), false);
		ret2.setCommit(true);
		assertEquals(ret2.isCommit(), true);
		// ret2 products list is empty, add t1, p1 with quantity 10, total cost 15.00
		ret2.getProducts().add(t1);
		assertEquals(ret2.getValue(), (Double)15.00);
	}
	
	@Test
	public void testAddEntryWithNull() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		ReturnTransaction ret1 = new ReturnTransaction(sale1);
		assertFalse(ret1.addEntry(null));
	}


	@Test
	public void testAddEntryWithValidInput() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		ReturnTransaction ret1 = new ReturnTransaction(sale1);
		ProductTypeImpl p1 = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...");
		TicketEntryImpl t1 = new TicketEntryImpl(p1, 10, 0.5);
		assertTrue(ret1.addEntry(t1));
		assertEquals((Integer)ret1.getProducts().size(), (Integer)1);
	}
	
	@Test
	public void testDeleteEntryWithNull() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		ReturnTransaction ret1 = new ReturnTransaction(sale1);
		assertNull(ret1.deleteEntry(null));

	}

	@Test
	public void testDeleteEntryWithProductNotPresent() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		ReturnTransaction ret1 = new ReturnTransaction(sale1);
		assertNull(ret1.deleteEntry("012345678912"));

	}

	@Test
	public void testDeleteEntryWithProductPresent() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		ReturnTransaction ret1 = new ReturnTransaction(sale1);
		ProductTypeImpl p1 = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...");
		TicketEntryImpl t1 = new TicketEntryImpl(p1, 10, 0.5);
		ret1.addEntry(t1);
		assertEquals(ret1.deleteEntry("012345678912"), t1);
	}

	@Test
	public void testDeleteEntryForLoop0Iterations() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		ReturnTransaction ret1 = new ReturnTransaction(sale1);
		assertNull(ret1.deleteEntry("012345678912"));
	}

	@Test
	public void testDeleteEntryForLoop1Iterations() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		ReturnTransaction ret1 = new ReturnTransaction(sale1);
		ProductTypeImpl p1 = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...");
		TicketEntryImpl t1 = new TicketEntryImpl(p1, 10, 0.5);
		ret1.addEntry(t1);
		assertEquals(ret1.deleteEntry("012345678912"), t1);
	}

	@Test
	public void testDeleteEntryForLoopMultipleIterations() {
		SaleTransactionImpl sale1= new SaleTransactionImpl();
		ReturnTransaction ret1 = new ReturnTransaction(sale1);
		ProductTypeImpl p1 = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...");
		ProductTypeImpl p2 = new ProductTypeImpl("0123456789128", "banana", 1.80, "banana notes...");
		TicketEntryImpl t1 = new TicketEntryImpl(p1, 10, 0.5);
		TicketEntryImpl t2 = new TicketEntryImpl(p2, 20, 0.0);
		ret1.addEntry(t1);
		ret1.addEntry(t2);
		assertEquals(ret1.deleteEntry("0123456789128"), t2);
	}



}
