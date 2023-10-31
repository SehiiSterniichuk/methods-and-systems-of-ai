package com.example.expertsystemservice.domain;

public enum RuleType {
    BINARY,//user gives an answer true or false, thenAction - true, elseAction - false
    BINARY_FORMULA,//true or false calculated by formula based on variables entered by user

    FORMULA,//user gives variables and rule calculate result by formula.
    // Each thenAction have own formula to calculate true/false value.
    // If each action is not satisfied in thenAction, so user receives elseAction in response
    VALUE_FORMULA//user gives variables and rule calculate result by formula.
    // Each thenAction have own formula to calculate probability (or anything)
    // User receives probabilities of each action and decide which action he wants to apply.
}
