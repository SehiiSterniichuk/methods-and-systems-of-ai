import ActionDTO from "./ActionDTO";

export type Variable = {
    name: string;
    value: string;
}

export enum DecisionStatus {
    SUCCESS = "SUCCESS",
    FAILED = "FAILED",
}

export type Decision = {
    value: string;
    status: DecisionStatus;
    action: ActionDTO;
}

export type DecisionResponse = {
    decisions: Decision[];
}

export type DecisionRequest = {
    ruleId: number;
    variables: Variable[];
}
