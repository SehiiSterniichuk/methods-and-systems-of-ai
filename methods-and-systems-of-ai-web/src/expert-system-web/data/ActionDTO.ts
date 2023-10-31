// TypeScript equivalent for DecisionInfo
import {RuleType} from "./ActionType";

export interface DecisionInfo {
    type: RuleType;
    formula?: string;
    variables: string[];
}

// TypeScript equivalent for ActionDTO
export interface ActionDTO {
    id?: number;
    name?: string;
    formula?: string;
    gotoAction?: RuleDTO[];
}


// TypeScript equivalent for RuleDTO
export interface RuleDTO {
    id?: number;
    name?: string;
    condition?: string;
    decisionInfo?: DecisionInfo;
    thenAction?: ActionDTO[];
    elseAction?: ActionDTO[];
}

export const defaultDecisionInfo: DecisionInfo = {
    type: RuleType.BINARY,
    variables: [],
};
export const defaultRule: RuleDTO = {condition: "", name: "", decisionInfo: defaultDecisionInfo};

export function returnEmptyRuleWithId(id: number): RuleDTO {
    return {id: id, condition: "", name: "", decisionInfo: {...defaultDecisionInfo}}
}

export function returnEmptyRuleOnlyWithName(name: string): RuleDTO {
    return {condition: "", name: name}
}

export function returnEmptyRuleOnlyWithId(id: number): RuleDTO {
    return {id: id}
}

export function returnEmptyActionWithId(id: number): ActionDTO {
    return {
        id: id,
        name: "",
        formula: "",
        gotoAction: []
    }
}

export default ActionDTO;