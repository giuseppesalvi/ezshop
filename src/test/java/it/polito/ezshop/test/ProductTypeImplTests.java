package it.polito.ezshop.test;

import it.polito.ezshop.model.ProductTypeImpl;
import org.junit.Test;


import java.util.Optional;

import static org.junit.Assert.*;

public class ProductTypeImplTests {

    // BB testing
	@Test
	public void testCheckBarCodeNull() {
        assertFalse(ProductTypeImpl.checkBarCode(null));
	}
    @Test
    public void testCheckBarCodeWithShortString(){
        // less than 12
        assertFalse(ProductTypeImpl.checkBarCode("123"));
        // boundary case 11 
        assertFalse(ProductTypeImpl.checkBarCode("12345678911"));
    }

    @Test
    public void testCheckBarCodeWithLongString(){
        // more than 14 
        assertFalse(ProductTypeImpl.checkBarCode("123456789123456789123456789"));
        // boundary case 15 
        assertFalse(ProductTypeImpl.checkBarCode("123456789123456"));
    }

    @Test
    public void testCheckBarCodeInvalidCheckSum(){
        assertFalse(ProductTypeImpl.checkBarCode("012345678911"));
    }

    @Test
    public void testCheckBarCodeValidString() {
        assertTrue(ProductTypeImpl.checkBarCode("012345678912"));
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

        ProductTypeImpl p2 = new ProductTypeImpl("0123456789126",
                "apple",
                1.50,
                "apple notes..."
        		);
        ProductTypeImpl p3 = new ProductTypeImpl("01234567891268");
        ProductTypeImpl p4 = new ProductTypeImpl(124);

        assertNotNull(p);
        assertNotNull(p2);
        assertNotNull(p3);
        assertNotNull(p4);

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
    
    @Test
    public void testCheckBarCodeForLoopString12() {
    	assertTrue(ProductTypeImpl.checkBarCode("012345678912"));
    	assertFalse(ProductTypeImpl.checkBarCode("012345678911"));
    }
 
    @Test
    public void testCheckBarCodeForLoopString13() {
    	assertTrue(ProductTypeImpl.checkBarCode("0123456789128"));
    	assertFalse(ProductTypeImpl.checkBarCode("0123456789127"));
    }

 
    @Test
    public void testCheckBarCodeForLoopString14() {
    	assertTrue(ProductTypeImpl.checkBarCode("01234567891286"));
    	assertFalse(ProductTypeImpl.checkBarCode("01234567891285"));
    }

}