export type TaskData = {
    taskId: number
}

export const FirstTask: TaskData = {
    taskId: 1,
}

export const enum TaskStatus {
    CREATE = "CREATE",
    SENDING = "SENDING",
    SUBMITTED = "SUBMITTED",
    DONE = "DONE",
}

export type TaskConfig = {
    iterationNumber: number;
    allowedNumberOfGenerationsWithTheSameResult: number;
    showEachIterationStep: number;
    populationSize: number;
    mutationProbability: number;
};
export const defaultConfig: TaskConfig = {
    iterationNumber: 1000,
    allowedNumberOfGenerationsWithTheSameResult: 10,
    showEachIterationStep: 10,
    populationSize: 20,
    mutationProbability: 10,
}
export type Point = {
    x: number;
    y: number;
};

export type Dataset = {
    data: Point[];
};

export type PostTaskRequest = {
    config: TaskConfig,
    dataset: Dataset
}

export type TaskId = {
    id: number;
};

export type Result = {
    path: Point[];
    pathLength: number;
};

export type ResultResponse = {
    result: Result;
    currentIteration: number;
    hasNext: boolean;
    message: string;
};

export const defaultResponse:ResultResponse = {
    result: {path: [], pathLength: -1},
    currentIteration: -1,
    hasNext: true,
    message: "",
}