import {SERVER_URL} from "../../travelling-salesman-web/data/Constants";
import {Pattern, PostRequest, PostTaskRequest} from "../data/Pattern";

export async function postPatterns(request: PostRequest) {
    return await fetch(`${SERVER_URL}/api/v1/hopfield/network`, {
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
        return response.text();
    }).then(x => Number.parseInt(x));
}
export async function getPattern(request: PostTaskRequest) {
    return await fetch(`${SERVER_URL}/api/v1/hopfield/task`, {
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
        return response.json();
    }).then(x => x as Pattern);
}