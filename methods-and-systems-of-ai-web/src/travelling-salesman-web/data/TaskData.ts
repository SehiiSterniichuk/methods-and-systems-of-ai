export type TaskData = {
    taskId: number
    taskConfig:TaskConfig
}

export const enum TaskStatus {
    CREATE = "CREATE",
    SENDING = "SENDING",
    CONNECTING = "CONNECTING",
    SUBMITTED = "SUBMITTED",
    DONE = "DONE",
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

export const defaultResponse: ResultResponse = {
    result: {path: [], pathLength: -1},
    currentIteration: -1,
    hasNext: true,
    message: "",
}

export type SearcherConfig = {
    distance: Distance;
    breedingType: BreedingType;
    diffPercent: number;
}

export enum Distance {
    HAMMING = "HAMMING",
    SCALAR = "SCALAR",
}

export enum BreedingType {
    INBREEDING = "INBREEDING",
    OUTBREEDING = "OUTBREEDING",
}
export enum CrossoverType {
    CYCLIC = "CYCLIC",
    ONE_POINT = "ONE_POINT",
}
export type TaskConfig = {
    iterationNumber: number;
    allowedNumberOfGenerationsWithTheSameResult: number;
    showEachIterationStep: number;
    populationSize: number;
    mutationProbability: number;
    searcherConfig: SearcherConfig;
    crossoverType: CrossoverType;
};
export const defaultSearchConfig: SearcherConfig = {
    breedingType: BreedingType.INBREEDING,
    diffPercent: 10,
    distance: Distance.SCALAR,
};
export const defaultConfig: TaskConfig = {
    iterationNumber: 1000,
    allowedNumberOfGenerationsWithTheSameResult: 10,
    showEachIterationStep: 10,
    populationSize: 20,
    mutationProbability: 10,
    searcherConfig: defaultSearchConfig,
    crossoverType: CrossoverType.CYCLIC
}
export type Statistic = {
    iteration: number;
    path: number;
    points: Point[];
}

export default SearcherConfig;
