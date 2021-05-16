package it.polito.ezshop.test;

import it.polito.ezshop.data.EZShopMaps;
import it.polito.ezshop.model.Position;
import it.polito.ezshop.model.ProductTypeImpl;
import org.junit.Test;


import static org.junit.Assert.*;

public class ProductTypeImplTests {
    @Test
    public void testCheckBarCode() {
        // TODO check separate functions
        assertFalse(ProductTypeImpl.checkBarCode("123"));
        assertFalse(ProductTypeImpl.checkBarCode(""));
        assertTrue(ProductTypeImpl.checkBarCode("978020137962"));
    }
    @Test
    public void testGetQuantity() {
        ProductTypeImpl productType = new ProductTypeImpl(123);
        productType.setQuantity(100);
        assertEquals(java.util.Optional.of(productType.getQuantity()),java.util.Optional.of(100));
    }
    @Test
    public void testSetQuantity() {
        // TODO add invalid inputs
    }
    @Test
    public void testGetLocation() {
        ProductTypeImpl productType = new ProductTypeImpl(
                null,
                null,
                null,
                null,
                null,
                "123-abc-123",
                null);
        assertEquals(productType.getLocation(),"123-abc-123");
    }
    @Test
    public void testSetLocation() {
        ProductTypeImpl productType = new ProductTypeImpl(
                null,
                null,
                null,
                null,
                null,
                "123-abc-123",
                null);
        EZShopMaps.loadMaps();
        productType.setLocation("456-abc-456");
        assertEquals(productType.getLocation(),"456-abc-456");
    }
    @Test
    public void testGetNote() {
        ProductTypeImpl productType = new ProductTypeImpl(123);
        productType.setNote("note");
        assertEquals(productType.getNote(),"note");
    }
    @Test
    public void testSetNote() {
        // TODO add invalid inputs
    }
    @Test
    public void testGetProductDescription() {
        ProductTypeImpl productType = new ProductTypeImpl(123);
        productType.setProductDescription("desc");
        assertEquals(productType.getProductDescription(),"desc");
    }
    @Test
    public void testSetProductDescription() {
        // TODO add invalid inputs
    }
    @Test
    public void testGetBarCode() {
        ProductTypeImpl productType = new ProductTypeImpl("978020137964");
        productType.setBarCode("108020137963");
        assertEquals(productType.getBarCode(),"108020137963");
    }
    @Test
    public void testSetBarCode() {
        // TODO add invalid inputs
    }
    @Test
    public void testGetPricePerUnit() {
        ProductTypeImpl productType = new ProductTypeImpl(123);
        productType.setPricePerUnit(10.50);
        assertEquals(java.util.Optional.of(productType.getPricePerUnit()),java.util.Optional.of(10.50));
    }
    @Test
    public void testSetPricePerUnit() {
        // TODO add invalid inputs
    }
    @Test
    public void testGetId() {
        assertEquals(java.util.Optional.of(new ProductTypeImpl(123).getId()),java.util.Optional.of(123));
    }
    @Test
    public void testSetId() {
        // TODO new class
        ProductTypeImpl productType = new ProductTypeImpl(
                null,
                null,
                null,
                null);
        EZShopMaps.loadMaps();
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
    }
}