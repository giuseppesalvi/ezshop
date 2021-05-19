package it.polito.ezshop.test;

import it.polito.ezshop.model.ProductTypeImpl;
import org.junit.Test;


import java.util.Optional;

import static org.junit.Assert.*;

public class ProductTypeImplTests {
    // BB testing
    @Test
    public void testCheckBarCodeValidString() {
        // 12 digit
        assertTrue(ProductTypeImpl.checkBarCode("012345678912"));
        // 13 digit
        assertTrue(ProductTypeImpl.checkBarCode("0123456789128"));
        // 14 digit
        assertTrue(ProductTypeImpl.checkBarCode("01234567891286"));
    }
    public void testCheckBarCodeInvalidString(){
        // less than 12
        assertFalse(ProductTypeImpl.checkBarCode("123"));
        // more than 14
        assertFalse(ProductTypeImpl.checkBarCode("012345678912356"));
        // null
        assertFalse(ProductTypeImpl.checkBarCode(null));
    }

    // WB testing
    @Test
    public void testCheckBarCodeInvalidCheckSum(){
        // 12 digit
        assertFalse(ProductTypeImpl.checkBarCode("012345678911"));
        // 13 digit
        assertFalse(ProductTypeImpl.checkBarCode("0123456789127"));
        // 14 digit
        assertFalse(ProductTypeImpl.checkBarCode("01234567891285"));
    }

    @Test
    public void testGettersSettersConstructors() {
        ProductTypeImpl p = new ProductTypeImpl(
                "012345678912",
                "apple",
                1.50,
                "apple notes...",
                100,
                "123-abc-123",
                false,
                1);

        // setGetQuantity
        p.setQuantity(20);
        assertEquals((Integer)p.getQuantity(), (Integer)20);
        // setGetLocation
        p.setLocation("456-abc-456");
        assertEquals(p.getLocation(),"456-abc-456");
        // setGetNote
        p.setNote("Note b aaa");
        assertEquals(p.getNote(), "Note b aaa");
        // setGetProductDescription
        p.setProductDescription("banana");
        assertEquals(p.getProductDescription(), "banana");
        // setGetBarCode
        p.setBarCode("108020137963");
        assertEquals(p.getBarCode(),"108020137963");
        // setGetPriceUnit
        p.setPricePerUnit(1.80);
        assertEquals((Double) p.getPricePerUnit(), (Double) 1.80);
        // setGetId
        p.setId(456);
        assertEquals(Optional.of(p.getId()), Optional.of(456));
        // setGetEliminated
        p.invertEliminated();
        assertEquals(p.getEliminated(),true);
        p.invertEliminated();
        assertEquals(p.getEliminated(),false);
    }
}