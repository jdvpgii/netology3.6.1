package com.gii.netology;

import org.apache.commons.beanutils.ConversionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.IllegalFormatException;

import static org.junit.jupiter.api.Assertions.*;

class BasketTest {
    @Test
    void loadFromTxtFile() {
        File file = new File("text.txt");

        Assertions.assertThrows(FileNotFoundException.class, () -> Basket.loadFromTxtFile(file));
    }

    @Test
    void testAddToCart() {
        String[] strings = {"asd", "pas"};
        int[] ints = {1, 2};
        Basket basket = new Basket(strings, ints);

        Assertions.assertNotNull(basket.getProductName());
    }
}