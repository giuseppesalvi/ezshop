package it.polito.ezshop.test;

import it.polito.ezshop.model.Product;
import it.polito.ezshop.model.ProductTypeImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProductTest {
    @Test
    public void testGettersSettersConstructors() {
        ProductTypeImpl p1 = new ProductTypeImpl("0123456789126",
                "apple",
                1.50,
                "apple notes..."
        );

        ProductTypeImpl p2 = new ProductTypeImpl("012345678912",
                "apple",
                1.50,
                "apple notes..."
        );

        Product pR = new Product(p1, "000000000010");
        assertEquals("000000000010", pR.getRFID());
        assertEquals("0123456789126", pR.getProductType().getBarCode());

        pR.setProductType(p2);
        assertEquals("012345678912", pR.getProductType().getBarCode());
    }
}
