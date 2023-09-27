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
    let response: Response;
    try {
        response = await fetch(`http://localhost:8080/api/v1/lab1/tasks/${id}`, {
            method: 'GET',
        });
    } catch (error) {
        throw error;
    }
    if (!response.ok) {
        response.text().then(t => console.log(`HTTP error! body: ${t}`))
        throw new Error(`HTTP error! Status: ${response.status}`);
    }
    return response;
}

function Task({webTaskId}: Props) {
    const [status, setStatus] = useState(TaskStatus.CREATE);
    const [coordinates, setCoordinates] = useState<Point[]>([]);
    const [result, setResult] = useState<Point[]>([]);
    const [pathLength, setPathLength] = useState(-1.0);
    const [betterThanPrevious, setBetterThanPrevious] = useState(-1.0);
    const [firstRes, setFirstRes] = useState(-1.0);
    const [allLength, setAllLength] = useState<Set<string>>(new Set());
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
                // response.text().then(t => console.log(`HTTP error! body: ${t}`))
                console.error(`HTTP error! Status: ${response.status}`);
                setHasNext(false);
            }
            return response;
        } catch (error) {
            throw error;
        }
    }

    function sendClicked() {
        if (TaskStatus.CREATE != status) {
            return;
        }
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
                if (status == TaskStatus.CONNECTING) {
                    setStatus(TaskStatus.SUBMITTED);
                }
                setHasNext(r.hasNext)
                setResult(r.result.path)
                const newPathLength = r.result.pathLength as number;
                const newPathLengthStr = newPathLength.toFixed(2);
                if (!allLength.has(newPathLengthStr)) {
                    console.log("newPathLength: " + newPathLength + " old: " + pathLength)
                    if (pathLength > 0) {
                        setBetterThanPrevious((pathLength / newPathLength - 1) * 100);
                    } else {
                        setFirstRes(newPathLength);
                    }
                    setPathLength(newPathLength)
                    setAllLength(() => {
                        const set = new Set(allLength);
                        set.add(newPathLengthStr);
                        return set;
                    });
                }
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

    function getAllResults() {
        if (allLength.size < 2) {
            return null;
        }
        const values = allLength.values()
        const numbers: number[] = new Array(allLength.size);
        for (let i = 0; i < allLength.size; i++) {
            const next = values.next().value as string;
            numbers[i] = Number.parseFloat(next);
        }
        return <div className={"list-of-selected-points"}>
            <p>Results: </p>
            <p>{numbers.join(', ')}</p>
        </div>;
    }

    const [connectId, setConnectId] = useState(-1);

    function getTaskId() {
        if (taskId != -1) {
            return <p className={"task-status"}>Task id: {taskId}</p>;
        }

        function inputId(x: React.FormEvent<HTMLInputElement>) {
            const currentTarget = x.currentTarget;
            if (currentTarget == null || currentTarget.value == null) {
                return;
            }
            const value = Number.parseInt(currentTarget.value);
            setConnectId(value)
        }

        function connectToTask() {
            if (connectId != -1) {
                setStatus(TaskStatus.CONNECTING);
                setTaskId(connectId);
            }
        }

        return <div className="task-status task-connector">
            <p>Task id: </p>
            <input className={"connect-id-input"} onInput={x => inputId(x)} type="number"/>
            <button onClick={connectToTask}>Connect</button>
        </div>;
    }

    function getBetterThanPrevious() {
        if (betterThanPrevious < 0) {
            return null;
        }
        return <p className={"task-status"}>Better than previous: +{betterThanPrevious.toFixed(2)}% </p>;
    }

    function getBetterThanFirst() {
        if (firstRes < 0) {
            return null;
        }
        const betterThanFirst = (firstRes / pathLength - 1) * 100;
        return <p className={"task-status"}>Better than first: +{betterThanFirst.toFixed(2)}% </p>;
    }

    return (
        <div className={"task"}>
            <TaskConfiguration sendClicked={sendClicked} config={config} setConfig={setConfig}
                               taskConfigId={webTaskId}/>
            <PathView calculatedPath={result} coordinates={coordinates} setCoordinates={setCoordinates}/>
            <div className={"list-of-selected-points"}>
                <p>Input: </p>
                <p>{coordinates.map(p => `(${p.x},${p.y})`).join(',')}</p>
            </div>
            <p className={"task-status"}>Status: {status.toString()}</p>

            {getTaskId()}
            <p className={"task-status"}>Current iteration:{iteration} </p>
            <p className={"task-status"}>Best path: {pathLength.toFixed(2)} </p>
            {getBetterThanPrevious()}
            {getBetterThanFirst()}
            <p className={"task-status"}>Message:{message} </p>
            <div className={"list-of-selected-points"}>
                <p>Output: </p>
                <p>{result.map(p => `(${p.x},${p.y})`).join(',')}</p>
            </div>
            {getAllResults()}
        </div>
    );
}

export default Task;