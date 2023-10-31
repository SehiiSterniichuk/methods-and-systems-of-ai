import React, {useState} from 'react';
import './App.css';
import './styles/App.scss';
import SearcherConfig, {
    BreedingType,
    CrossoverType,
    defaultConfig,
    Distance,
    TaskConfig, TaskData
} from "./data/TaskData";
import Task from "./components/Task";

function TravellingSalesmanApp() {
    const scalarInbreeding: SearcherConfig = {
        breedingType: BreedingType.INBREEDING,
        diffPercent: 33,
        distance: Distance.SCALAR,
    };
    const hammingOutbreeding: SearcherConfig = {
        breedingType: BreedingType.OUTBREEDING,
        diffPercent: 33,
        distance: Distance.HAMMING,
    };
    const scalarOutbreeding: SearcherConfig = {
        breedingType: BreedingType.OUTBREEDING,
        diffPercent: 33,
        distance: Distance.SCALAR,
    };
    const hammingInbreeding: SearcherConfig = {
        breedingType: BreedingType.INBREEDING,
        diffPercent: 33,
        distance: Distance.HAMMING,
    };
    const studyArray16Configs_Cyclic: TaskConfig[] = [
        {
            iterationNumber: 100_000_000,
            allowedNumberOfGenerationsWithTheSameResult: 10_000,
            showEachIterationStep: 2500,
            populationSize: 64,
            mutationProbability: 22,
            searcherConfig: scalarInbreeding,
            crossoverType: CrossoverType.CYCLIC
        },
        {
            iterationNumber: 100_000_000,
            allowedNumberOfGenerationsWithTheSameResult: 10_000,
            showEachIterationStep: 2500,
            populationSize: 64,
            mutationProbability: 22,
            searcherConfig: hammingOutbreeding,
            crossoverType: CrossoverType.CYCLIC
        },
        {
            iterationNumber: 100_000_000,
            allowedNumberOfGenerationsWithTheSameResult: 10_000,
            showEachIterationStep: 2500,
            populationSize: 64,
            mutationProbability: 22,
            searcherConfig: scalarOutbreeding,
            crossoverType: CrossoverType.CYCLIC
        },
        {
            iterationNumber: 100_000_000,
            allowedNumberOfGenerationsWithTheSameResult: 10_000,
            showEachIterationStep: 2500,
            populationSize: 64,
            mutationProbability: 22,
            searcherConfig: hammingInbreeding,
            crossoverType: CrossoverType.CYCLIC
        },
    ]
    const studyArray16Configs_OnePoint = studyArray16Configs_Cyclic
        .map(c => ({...c, crossoverType: CrossoverType.ONE_POINT}))
    const studyArray16Configs_All = studyArray16Configs_Cyclic
        .concat(studyArray16Configs_OnePoint);
    const studyArray64Configs_All = studyArray16Configs_All.map(t=>{
        return {...t, populationSize: 2048, showEachIterationStep: 100, allowedNumberOfGenerationsWithTheSameResult: 1000}
    })
    const studyNow = [defaultConfig];
    const [taskData, setTaskData] = useState(studyNow.map(x => {
        const task: TaskData = {
            taskId: 0,
            taskConfig: x
        }
        return task;
    }));
    for (let i = 0; i < taskData.length; i++) {
        taskData[i].taskId = i;
    }
    const taskList = taskData.map(t => (<Task defaultConfig={t.taskConfig} webTaskId={t.taskId}/>));
    return (
        <div className="App App-header salesman-body">
            <h1>Traversal salesman problem</h1>
            <div className="task-wrapper">
                <h2 className={"task-list-title"}>Tasks: </h2>
                <div className="task-list">
                    {taskList}
                </div>
            </div>
        </div>
    );
}

export default TravellingSalesmanApp;
