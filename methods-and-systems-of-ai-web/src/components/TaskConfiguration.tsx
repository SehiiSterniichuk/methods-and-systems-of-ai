import React, {useState} from 'react';
import "../styles/TaskConfiguration.scss";
import {TaskConfig} from "../data/TaskData";

interface Props {
    taskConfigId: number;
    setConfig: (value: (((prevState: TaskConfig) => TaskConfig) | TaskConfig)) => void;
    config: TaskConfig;
    sendClicked: () => void,
}

export type TaskInput = {
    name: string
    defaultValue: number
    min: number
    max: number
    setter: (x: React.FormEvent<HTMLInputElement>) => void;
}


function TaskConfiguration({taskConfigId, setConfig, config, sendClicked}: Props,) {
    const inputs: TaskInput[] =
        [
            {
                name: "Iteration number",
                defaultValue: config.iterationNumber,
                setter: setIterations,
                min: 1,
                max: 10_000_000_000
            },
            {name: "Mutation percent", defaultValue: config.mutationProbability, setter: setMutation, min: 0, max: 100},
            {
                name: "Population size",
                defaultValue: config.populationSize,
                setter: setPopulation,
                min: 10,
                max: 100
            },
            {
                name: "Show iteration",
                defaultValue: config.showEachIterationStep,
                setter: setShow,
                min: 1,
                max: 10_000_000
            },
            {
                name: "Allowed repeating result",
                defaultValue: config.allowedNumberOfGenerationsWithTheSameResult,
                setter: setAllowed,
                min: 0,
                max: 10_000
            },]

    function setIterations(x: React.FormEvent<HTMLInputElement>) {
        if (x.currentTarget.value == null) {
            return;
        }
        let iterationNumber = Number.parseInt(x.currentTarget.value);
        if (iterationNumber === null) {
            return;
        }
        setConfig(c => {
            const newConfig: TaskConfig = {...c, iterationNumber: iterationNumber};
            return newConfig;
        });
    }

    function setMutation(x: React.FormEvent<HTMLInputElement>) {
        if (x.currentTarget.value == null) {
            return;
        }
        let mutationProbability = Number.parseInt(x.currentTarget.value);
        if (mutationProbability == null) {
            return;
        }
        setConfig(c => {
            const newConfig: TaskConfig = {...c, mutationProbability: mutationProbability};
            return newConfig;
        });
    }

    function setPopulation(x: React.FormEvent<HTMLInputElement>) {
        if (x.currentTarget.value == null) {
            return;
        }
        let populationSize = Number.parseInt(x.currentTarget.value);
        if (populationSize === null) {
            return;
        }
        setConfig(c => {
            const newConfig: TaskConfig = {...c, populationSize: populationSize};
            return newConfig;
        });
    }

    function setShow(x: React.FormEvent<HTMLInputElement>) {
        if (x.currentTarget.value == null) {
            return;
        }
        let showEachIterationStep = Number.parseInt(x.currentTarget.value);
        if (showEachIterationStep === null) {
            return;
        }
        setConfig(c => {
            const newConfig: TaskConfig = {...c, showEachIterationStep: showEachIterationStep};
            return newConfig;
        });
    }

    function setAllowed(x: React.FormEvent<HTMLInputElement>) {
        if (x.currentTarget.value == null) {
            return;
        }
        let allowed = Number.parseInt(x.currentTarget.value);
        if (allowed === null) {
            return;
        }
        setConfig(c => {

            const newConfig: TaskConfig = {
                ...c,
                allowedNumberOfGenerationsWithTheSameResult: allowed
            };
            return newConfig;
        });
    }

    let inputElements = inputs.map(t => {
        let i = t.name;
        let id = i.replace(" ", "_").toLowerCase();
        return (
            <div className={`input-config specific_input_${id}`}>
                <p>{i}: </p>
                <input type="number" defaultValue={t.defaultValue}
                       min={t.min} max={t.max} id={id + taskConfigId}
                       onInput={x => {
                           t.setter(x)
                           console.log(x);
                           console.log(x.target);
                       }
                       }
                />
            </div>
        );
    });
    const [showButton, setShowButton] = useState(true);

    function onClickButton() {
        if (showButton) {
            sendClicked();
            setShowButton(false);
            return;
        }
    }

    let button = showButton ? <button onClick={onClickButton}>Start</button> : null;
    return (
        <div className={"task-configuration"}>
            <div className="inputs">
                {inputElements}
            </div>
            {button}
        </div>
    );
}

export default TaskConfiguration;