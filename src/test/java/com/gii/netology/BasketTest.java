package com.gii.netology;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.FileNotFoundException;

class BasketTest {
    @Test
    void loadFromTxtFile() {
        File file = new File("text.txt");

        Assertions.assertThrows(FileNotFoundException.class, () -> Basket.loadFromTxtFile(file));
    }

    @ParameterizedTest
    @ValueSource(strings = {"3", "4"})
    void addToCart(String str) {
        Basket basket = new Basket(new String[]{"a", "b"}, new int[] {1, 2});
        Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> basket.addToCart(Integer.parseInt(str), 3));
    }
}