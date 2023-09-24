import React from 'react';
import "../styles/TaskConfiguration.scss";

interface Props {
    taskConfigId: number;
}

export type TaskInput = {
    name: string
    defaultValue: number
    min: number
}
const inputs: TaskInput[] =
    [
        {name: "Iteration number", defaultValue: 1000, min: 1},
        {name: "Number of random cities", defaultValue: -1, min: -1},
        {name: "Mutation percent", defaultValue: 10, min: 0},
        {name: "Show iteration", defaultValue: 200, min: 1},
        {name: "Allowed repeating result", defaultValue: 20, min: 0},]

function TaskConfiguration({taskConfigId}: Props) {
    let inputElements = inputs.map(t => {
        let i = t.name;
        let id = i.replace(" ", "_").toLowerCase();
        return (
            <div className={`input-config specific_input_${id}`}>
                <p>{i}: </p>
                <input type="number" defaultValue={t.defaultValue}
                       min={t.min} id={id + taskConfigId} onInput={x => onInputV1(x)}/>
            </div>
        );
    });
    return (
        <div className={"task-configuration"}>
            <div className="inputs">
                {inputElements}
            </div>
            <button>Start</button>
        </div>
    );
}

export default TaskConfiguration;