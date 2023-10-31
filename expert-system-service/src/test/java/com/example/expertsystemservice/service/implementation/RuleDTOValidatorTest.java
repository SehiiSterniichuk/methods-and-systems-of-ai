package com.example.expertsystemservice.service.implementation;

import org.junit.jupiter.api.Test;

import static com.example.expertsystemservice.service.implementation.DTOConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class RuleDTOValidatorTest {
    private final RuleDTOValidator validator = new RuleDTOValidator();
    @Test
    void isValidBinary() {
        assertEquals("", validator.isValid(binaryRule));
    }
    @Test
    void isValidBinaryFormula() {
        assertEquals("", validator.isValid(parabolaBinaryFormulaRule));
    }

    @Test
    void isValidValueFormula() {
        assertEquals("", validator.isValid(parabolaValueFormulaRule));
    }

    @Test
    void isValidFormula() {
        assertEquals("", validator.isValid(parabolaFormulaRule));
    }
}