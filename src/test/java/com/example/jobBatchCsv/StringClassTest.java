package com.example.jobBatchCsv;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class StringClassTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getStringValue() {
    }

    @Test
    public void testNullInput(){
        StringClass stringClass = new StringClass();
        assertThrows(IllegalArgumentException.class, ()->{
            stringClass.getStringValue(null);
        });
    }

    @Test
    public  void testEmptyInput(){
        StringClass stringClass = new StringClass();
        assertEquals("Empty String",stringClass.getStringValue(""));
    }

    @Test
    public  void testWhiteSpaceInput(){
        StringClass stringClass = new StringClass();
        assertEquals("Whitespace",stringClass.getStringValue("  "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Hello","chicken"})
    public void testValidInput(String word){
        StringClass stringClass = new StringClass();

        assertEquals(word.toUpperCase(),stringClass.getStringValue(word));

    }


}