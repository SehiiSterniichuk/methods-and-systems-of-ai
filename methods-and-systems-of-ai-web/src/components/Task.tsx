import React, {useState} from 'react';
import TaskConfiguration from "./TaskConfiguration";
import {
    defaultConfig,
    defaultResponse,
    Point,
    PostTaskRequest,
    ResultResponse,
    Statistic,
    TaskStatus
} from "../data/TaskData";
import '../styles/Task.scss';
import PathView from "./PathView";
import {SERVER_URL} from "../data/Constants";

interface Props {
    webTaskId: number;
}

// Function to create a task


async function getTask(id: string) {
    let response: Response;
    try {
        response = await fetch(`${SERVER_URL}/api/v1/lab1/tasks/${id}`, {
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

async function fetchStatistic(id: string) {
    let response: Response;
    try {
        response = await fetch(`${SERVER_URL}/api/v1/lab1/task-storage/statistic/${id}`, {
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
    const [betterThanFirst, setBetterThanFirstRes] = useState(-1.0);
    const [statistics, setStatistics] = useState<Statistic[]>([]);
    const [hasNext, setHasNext] = useState(true);
    const [message, setMessage] = useState("");
    const [iteration, setIteration] = useState(-1);
    const [taskId, setTaskId] = useState<string>("-1");
    const [config, setConfig] = useState(defaultConfig);
    const [response, setResponse] = useState(defaultResponse);

    async function createTask(request: PostTaskRequest) {
        try {
            const response = await fetch(`${SERVER_URL}/api/v1/lab1/tasks/`, {
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
        if (TaskStatus.CREATE !== status) {
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
        function updateStatistics() {
            fetchStatistic(taskId)
                .then(x => x.json())
                .then(x => x as Statistic[])
                .then(list => {
                    list.sort((x, y) => {
                        return x.iteration - y.iteration
                    })
                    setStatistics(list);
                    if (list.length >= 2) {
                        const last = list[list.length - 1].path;
                        setBetterThanPrevious(((last / list[list.length - 2].path) - 1) * 100);
                        setBetterThanFirstRes(((list[0].path / last) - 1) * 100)
                    }
                })
        }

        getTask(taskId)
            .then(x => x.json())
            .then(r=>r as ResultResponse)
            .then(r => {
                if (status === TaskStatus.CONNECTING) {
                    setStatus(TaskStatus.SUBMITTED);
                }
                setHasNext(r.hasNext)
                setResult(r.result.path)
                setPathLength(r.result.pathLength as number)
                if (r.message.includes("best")) {
                    updateStatistics();
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

    if (taskId !== "-1" && response.hasNext && hasNext && status !== TaskStatus.DONE) {
        update();
    }

    function getAllResults() {
        if (statistics.length < 2) {
            return null;
        }
        return <div className={"list-of-selected-points"}>
            <p>Results: </p>
            <p>{statistics.map(x=>x.path.toFixed(2)).join(', ')}</p>
        </div>;
    }

    const [connectId, setConnectId] = useState("-1");

    function getTaskId() {
        if (taskId !== "-1") {
            return <p className={"task-status"}>Task id: {taskId}</p>;
        }

        function inputId(x: React.FormEvent<HTMLInputElement>) {
            const currentTarget = x.currentTarget;
            if (currentTarget == null || currentTarget.value == null) {
                return;
            }
            setConnectId(currentTarget.value)
        }

        function connectToTask() {
            if (connectId !== "-1") {
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
        if (betterThanFirst < 0) {
            return null;
        }
        return <p className={"task-status"}>Better than first: +{betterThanFirst.toFixed(2)}% </p>;
    }

    function makeAChart() {
        if(status != TaskStatus.DONE){
            return null;
        }
        return <img className={"chart-img"} src={`${SERVER_URL}/api/v1/lab1/chart/generate/${taskId}`} alt="chart"/>;
    }

    function getLastSuccessfulIteration() {
        if(statistics.length < 1){
            return null;
        }
        return <p className={"task-status"}>Last successful iteration: {statistics[statistics.length - 1].iteration}</p>
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
            {getLastSuccessfulIteration()}
            <p className={"task-status"}>Message:{message} </p>
            <div className={"list-of-selected-points"}>
                <p>Output: </p>
                <p>{result.map(p => `(${p.x},${p.y})`).join(',')}</p>
            </div>
            {getAllResults()}
            {makeAChart()}
        </div>
    );
}

export default Task;