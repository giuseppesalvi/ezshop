package it.polito.ezshop.test;

import it.polito.ezshop.model.Position;
import org.junit.Test;
import it.polito.ezshop.exceptions.InvalidPositionException;

import static org.junit.Assert.*;

public class PositionTests {

    @Test
    public void testSettersGetters() throws InvalidPositionException {
        // test valid string
        Position position1 = new Position("123","abc","123");
        position1.setPosition("456-def-456");
        assertEquals(position1.getPosition(),"456-def-456");
        // test invalid string
        Position position2 = new Position("123-abc-123");
        assertThrows(InvalidPositionException.class, () -> { position2.setPosition("456def456"); });
        assertThrows(InvalidPositionException.class, () -> { position2.setPosition(""); });
    }

}