export enum ActionType {
    THEN, ELSE
}

export enum ActionDecisionType{
    GOTO =  "GOTO",
    RESULT = "RESULT"
}

export enum RuleType {
    BINARY = "BINARY",
    BINARY_FORMULA = "BINARY_FORMULA",
    FORMULA = "FORMULA",
    VALUE_FORMULA = "VALUE_FORMULA"
}

export const ruleTypes: RuleType[] = [RuleType.BINARY, RuleType.BINARY_FORMULA, RuleType.VALUE_FORMULA, RuleType.FORMULA];
export const actionTypes: ActionDecisionType[] = [ActionDecisionType.GOTO, ActionDecisionType.RESULT];

export default ActionType;