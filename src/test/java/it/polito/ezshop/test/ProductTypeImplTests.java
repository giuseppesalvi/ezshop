package it.polito.ezshop.test;

import it.polito.ezshop.model.Position;
import it.polito.ezshop.model.ProductTypeImpl;
import org.junit.Test;


import static org.junit.Assert.*;

public class ProductTypeImplTests {
    @Test
    public void testGetQuantity() {
        ProductTypeImpl p = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...", 100, "123-abc-123", false, 1);
        assertEquals(java.util.Optional.of(p.getQuantity()),java.util.Optional.of(100));
    }
    @Test
    public void testSetQuantity() {
    	ProductTypeImpl p = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...");
    	p.setQuantity(20);
    	assertEquals((Integer)p.getQuantity(), (Integer)20);
    }
    @Test
    public void testGetLocation() {
        ProductTypeImpl p = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...", 100, "123-abc-123", false, 1);
        assertEquals(p.getLocation(),"123-abc-123");
    }
    @Test
    public void testSetLocation() {
    	ProductTypeImpl p = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...", 100, "123-abc-123", false, 1);
        p.setLocation("456-abc-456");
        assertEquals(p.getLocation(),"456-abc-456");
    }
    @Test
    public void testGetNote() {
    	ProductTypeImpl p = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...", 100, "123-abc-123", false, 1);
        assertEquals(p.getNote(),"apple notes...");
    }
    @Test
    public void testSetNote() {
    	ProductTypeImpl p = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...", 100, "123-abc-123", false, 1);
    	p.setNote("Note b aaa");
    	assertEquals(p.getNote(), "Note b aaa");
    }
    @Test
    public void testGetProductDescription() {
    	ProductTypeImpl p = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...", 100, "123-abc-123", false, 1);
        assertEquals(p.getProductDescription(),"apple");
    }
    @Test
    public void testSetProductDescription() {
    	ProductTypeImpl p = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...", 100, "123-abc-123", false, 1);
    	p.setProductDescription("banana");
    	assertEquals(p.getProductDescription(), "banana");
    }
    @Test
    public void testGetBarCode() {
    	ProductTypeImpl p = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...", 100, "123-abc-123", false, 1);
        assertEquals(p.getBarCode(),"012345678912");
    }
    @Test
    public void testSetBarCode() {
    	ProductTypeImpl p= new ProductTypeImpl("978020137964");
        p.setBarCode("108020137963");
        assertEquals(p.getBarCode(),"108020137963");

    }
    @Test
    public void testGetPricePerUnit() {
    	ProductTypeImpl p = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...", 100, "123-abc-123", false, 1);
    	assertEquals((Double) p.getPricePerUnit(), (Double) 1.50);
    }
    @Test
    public void testSetPricePerUnit() {
    	ProductTypeImpl p = new ProductTypeImpl("012345678912", "apple", 1.50, "apple notes...", 100, "123-abc-123", false, 1);
    	p.setPricePerUnit(1.80);
    	assertEquals((Double) p.getPricePerUnit(), (Double) 1.80);

    }
    @Test
    public void testGetId() {
        assertEquals(java.util.Optional.of(new ProductTypeImpl(123).getId()),java.util.Optional.of(123));
    }
    @Test
    public void testSetId() {
        ProductTypeImpl productType = new ProductTypeImpl(
                null,
                null,
                null,
                null);
        productType.setId(456);
        assertEquals(java.util.Optional.of(productType.getId()),java.util.Optional.of(456));
    }
    @Test
    public void testGetEliminated() {
        assertEquals(new ProductTypeImpl(123).getEliminated(),false);
    }
    @Test
    public void testInvertEliminated() {
        ProductTypeImpl productType = new ProductTypeImpl(123);
        productType.invertEliminated();
        assertEquals(productType.getEliminated(),true);
        productType.invertEliminated();
        assertEquals(productType.getEliminated(),false);
    }
}