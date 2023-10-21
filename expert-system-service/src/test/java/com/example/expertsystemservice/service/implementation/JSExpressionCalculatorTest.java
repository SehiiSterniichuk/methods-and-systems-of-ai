package com.example.expertsystemservice.service.implementation;

import com.example.expertsystemservice.service.ExpressionCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JSExpressionCalculatorTest {
    private final ExpressionCalculator calculator = new JSExpressionCalculator();

    @Test
    void evaluate() {
        String evaluate = calculator.evaluate("1+1");
        assertEquals("2", evaluate);
    }

    @Test
    void evaluateToBoolean() {
        assertTrue(calculator.evaluateToBoolean("1<2"));
        assertTrue(calculator.evaluateToBoolean("1<=2"));
        assertTrue(calculator.evaluateToBoolean("  1+  1<=  2"));
    }
}