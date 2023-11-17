export type Pattern = {
    p: number[][]
}

export function getMatrix(n: number): Pattern {
    const twoDArray: number[][] = Array
        .from({length: n},
            () => new Array(n).fill(0));

    return {p: twoDArray};
}

export type PostRequest = {
    name:string,
    patterns:Pattern[]
}

export type PostTaskRequest = {
    networkName:string,
    pattern:Pattern
}