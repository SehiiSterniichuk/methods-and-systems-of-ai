import {SERVER_URL} from "../../travelling-salesman-web/data/Constants";
import {RuleDTO} from "../data/ActionDTO";

type PostRuleRequest = {
    rules: RuleDTO[]
}

export async function sendRules(rules: RuleDTO[]) {
    const request: PostRuleRequest = {rules: rules};
    try {
        const response = await fetch(`${SERVER_URL}/api/v1/expert-system/rule`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(request),
        });
        if (!response.ok) {
            const status = response.status;
            console.error(`HTTP error! Status: ${status}`);
        }
        return response;
    } catch (error) {
        throw error;
    }
}

export async function findQuestions(string: string) {
    const response = await fetch(`${SERVER_URL}/api/v1/expert-system/question/${string}`, {
        method: 'GET',
    });
    if (!response.ok) {
        const status = response.status;
        console.error(`HTTP error! Status: ${status}`);
    }
    return response;
}

export async function getRuleById(id: number) {
    const response = await fetch(`${SERVER_URL}/api/v1/expert-system/rule/${id}/2`, {
        method: 'GET',
    });
    if (!response.ok) {
        const status = response.status;
        console.error(`HTTP error! Status: ${status}`);
    }
    return response;
}