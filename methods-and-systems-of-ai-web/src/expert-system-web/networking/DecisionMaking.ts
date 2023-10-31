import {SERVER_URL} from "../../travelling-salesman-web/data/Constants";
import {DecisionRequest, DecisionResponse} from "../data/Decision";

export async function getOutputVariable() {
    const response = await fetch(`${SERVER_URL}/api/v1/expert-system/decision/output-variable-name`, {
        method: 'GET',
    });
    if (!response.ok) {
        const status = response.status;
        console.error(`HTTP error! Status: ${status}`);
    }
    return response;
}

export async function makeDecision(request: DecisionRequest) {
    return await fetch(`${SERVER_URL}/api/v1/expert-system/decision`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(request),
    }).then(response => {
        if (!response.ok) {
            const status = response.status;
            console.error(`HTTP error! Status: ${status}`);
        }
        return response.json()
    }).then(x => x as DecisionResponse);
}