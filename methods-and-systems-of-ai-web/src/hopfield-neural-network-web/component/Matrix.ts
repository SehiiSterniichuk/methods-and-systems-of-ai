export type Matrix = {
    array: number[][]
}

export function getMatrix(n: number): Matrix {
    const twoDArray: number[][] = Array
        .from({length: n},
            () => new Array(n).fill(0));

    return {array: twoDArray};
}