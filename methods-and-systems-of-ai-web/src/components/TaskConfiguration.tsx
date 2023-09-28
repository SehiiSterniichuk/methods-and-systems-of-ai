import React, {useState} from 'react';
import "../styles/TaskConfiguration.scss";
import {BreedingType, Distance, TaskConfig} from "../data/TaskData";

interface Props {
    taskConfigId: number;
    setConfig: (value: (((prevState: TaskConfig) => TaskConfig) | TaskConfig)) => void;
    config: TaskConfig;
    sendClicked: () => void,
}

export type TaskInput = {
    name: string
    defaultValue: number
    min?: number
    max?: number
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
            },
        ]

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
                <p className={"input-title"}>{i}: </p>
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

    function selectBreedingType() {
        function setBreedingType(x: React.ChangeEvent<HTMLSelectElement>) {
            if (x.currentTarget == null) return;
            const value = x.currentTarget.value;
            if (value == null || value.length < 1) {
                return;
            }
            setConfig(c => {
                const breedingType = BreedingType.INBREEDING === value ? BreedingType.INBREEDING : BreedingType.OUTBREEDING;
                const newConfig: TaskConfig = {...c, searcherConfig: {...c.searcherConfig, breedingType: breedingType}};
                return newConfig;
            });
        }

        return (
            <div className={"input-config selector-input"} id={`breeding_selector_${taskConfigId}`}>
                <label className={"input-title"} htmlFor={`breedingType_${taskConfigId}`}>Breeding:</label>
                <select id={`breedingType_${taskConfigId}`} value={config.searcherConfig.breedingType}
                        onChange={setBreedingType}>
                    <option value={BreedingType.INBREEDING}>Inbreeding</option>
                    <option value={BreedingType.OUTBREEDING}>Outbreeding</option>
                </select>
            </div>
        );
    }
    function selectDistanceType() {
        function setDistanceType(x: React.ChangeEvent<HTMLSelectElement>) {
            if (x.currentTarget == null) return;
            const value = x.currentTarget.value;
            if (value == null || value.length < 1) {
                return;
            }
            setConfig(c => {
                const distance = Distance.SCALAR === value ? Distance.SCALAR : Distance.HAMMING;
                const newConfig: TaskConfig = {...c, searcherConfig: {...c.searcherConfig, distance: distance}};
                return newConfig;
            });
        }

        return (
            <div className={"input-config selector-input"} id={`breeding_selector_${taskConfigId}`}>
                <label className={"input-title"} htmlFor={`distance_${taskConfigId}`}>Distance:</label>
                <select id={`distance_${taskConfigId}`} value={config.searcherConfig.distance}
                        onChange={setDistanceType}>
                    <option value={Distance.SCALAR}>Scalar</option>
                    <option value={Distance.HAMMING}>Hamming</option>
                </select>
            </div>
        );
    }
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
                {selectDistanceType()}
                {selectBreedingType()}
            </div>
            {button}
        </div>
    );
}

export default TaskConfiguration;