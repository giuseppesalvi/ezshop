package it.polito.ezshop.test;

import it.polito.ezshop.model.Position;
import org.junit.Test;
import it.polito.ezshop.exceptions.InvalidPositionException;

import static org.junit.Assert.*;

public class PositionTests {

    @Test
    public void testSetPositionWithValidString() throws InvalidPositionException {
        // I can set valid string without errors.
        Position position = new Position("123-abc-123");
        position.setPosition("456-def-456");
        assertEquals(position.getPosition(),"456-def-456");
    }

    @Test
    public void testSetPositionWithInvalidString() {
        // Invalid Position should throw exception
        Position position = new Position("123-abc-123");
        assertThrows(InvalidPositionException.class, () -> { position.setPosition("456def456"); });
        assertThrows(InvalidPositionException.class, () -> { position.setPosition(""); });
    }
}