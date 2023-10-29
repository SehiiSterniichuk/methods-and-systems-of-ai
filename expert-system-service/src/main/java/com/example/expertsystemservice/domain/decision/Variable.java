package com.example.expertsystemservice.domain.decision;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record Variable(
        @NotBlank
        String name,
        @NotBlank
        String value) {
    public final static String OUTPUT_VARIABLE = "output";
    public final static String VARIABLE_POSTFIX = "_v";

//    public final static Variable outputTrue = new Variable(OUTPUT_VARIABLE, "yes");
    public final static Variable outputFalse = new Variable(OUTPUT_VARIABLE, "no");
//    public final static List<Variable> outputListTrue = List.of(outputTrue);
    public final static List<Variable> outputListFalse = List.of(outputFalse);
}
