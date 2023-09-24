import React, {useState} from 'react';
import TaskConfiguration from "./TaskConfiguration";
import {defaultConfig, defaultResponse, Point, PostTaskRequest, TaskStatus} from "../data/TaskData";
import '../styles/Task.scss';
import PathView from "./PathView";

interface Props {
    webTaskId: number;
}

// Function to create a task


async function getTask(id: number) {
    try {
        const response = await fetch(`http://localhost:8080/api/v1/lab1/tasks/${id}`, {
            method: 'GET',
        });
        if (!response.ok) {
            response.text().then(t => console.log(`HTTP error! body: ${t}`))
            throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response;
    } catch (error) {
        throw error;
    }
}

function Task({webTaskId}: Props) {
    const [status, setStatus] = useState(TaskStatus.CREATE);
    const [coordinates, setCoordinates] = useState<Point[]>([]);
    const [result, setResult] = useState<Point[]>([]);
    const [pathLength, setPathLength] = useState(-1.0);
    const [hasNext, setHasNext] = useState(true);
    const [message, setMessage] = useState("");
    const [iteration, setIteration] = useState(-1);
    const [taskId, setTaskId] = useState<number>(-1);
    const [config, setConfig] = useState(defaultConfig);
    const [response, setResponse] = useState(defaultResponse);

    async function createTask(request: PostTaskRequest) {
        try {
            const response = await fetch('http://localhost:8080/api/v1/lab1/tasks/', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(request),
            });
            if (!response.ok) {
                response.text().then(t => console.log(`HTTP error! body: ${t}`))
                console.error(`HTTP error! Status: ${response.status}`);
                setHasNext(false);
            }
            return response;
        } catch (error) {
            throw error;
        }
    }

    function sendClicked() {
        const mutationProbability = config.mutationProbability / 100;
        const sendConfig = {...config, mutationProbability: mutationProbability}
        const request: PostTaskRequest = {config: sendConfig, dataset: {data: coordinates}};
        setStatus(TaskStatus.SENDING);
        createTask(request)
            .then(response => response.json())
            .then(x => x.id)
            .then(id => {
                setTaskId(id)
                setStatus(TaskStatus.SUBMITTED)
                return id;
            });
    }

    function update() {
        getTask(taskId)
            .then(x => x.json())
            .then(r => {
                setHasNext(r.hasNext)
                setResult(r.result.path)
                setPathLength(r.result.pathLength)
                if (!r.hasNext) {
                    setStatus(TaskStatus.DONE);
                }
                if (r.message !== null && r.message.length > 0) {
                    setMessage(r.message)
                }
                setIteration(r.currentIteration);
                setResponse(r);
            }).catch(e => {
            console.error("handled error");
            console.error(e);
        })
    }

    if (taskId !== -1 && response.hasNext && hasNext && status != TaskStatus.DONE) {
        update();
    }
    return (
        <div className={"task"}>
            <TaskConfiguration sendClicked={sendClicked} config={config} setConfig={setConfig}
                               taskConfigId={webTaskId}/>
            <PathView coordinates={coordinates} setCoordinates={setCoordinates}/>
            <div className={"list-of-selected-points"}>
                <p>Input: </p>
                <p>{coordinates.map(p => `(${p.x},${p.y})`).join(',')}</p>
            </div>
            <p className={"task-status"}>Status: {status.toString()}</p>
            <p className={"task-status"}>Task id: {taskId}</p>
            <p className={"task-status"}>Current iteration:{iteration} </p>
            <p className={"task-status"}>Message:{message} </p>
            <div className={"list-of-selected-points"}>
                <p>Output: </p>
                <p>{result.map(p => `(${p.x},${p.y})`).join(',')}</p>
            </div>
        </div>
    );
}

export default Task;