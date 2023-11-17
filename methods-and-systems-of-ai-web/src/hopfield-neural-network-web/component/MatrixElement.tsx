import React, {useEffect, useState} from 'react';
import {Matrix} from "./Matrix";

interface Props {
    matrix: Matrix,
    row: number,
    col: number,
    value: number,
    parentWidth: number,
    parentHeight: number,
}

function MatrixElement({matrix, row, col, value, parentHeight, parentWidth}: Props) {
    const [selected, setSelected] = useState(value > 0);
    useEffect(() => {
        setSelected(matrix.array[row][col] > 0);
    }, [matrix]);

    function clickElement(row: number, col: number) {
        matrix.array[row][col] = selected ? 0 : 1;
        setSelected(!selected);
    }

    function dragOver(row: number, col: number) {
        if(selected){
            return;
        }
        clickElement(row, col);
    }

    return <div
        onDragOver={e => dragOver(row, col)}
        onClick={e => clickElement(row, col)}
        style={{width: parentWidth / matrix.array.length, height: parentWidth / matrix.array.length}}
        className={`matrix-element ${selected ? "selected-element" : "unselected-element"}`}>
    </div>
}

export default MatrixElement;