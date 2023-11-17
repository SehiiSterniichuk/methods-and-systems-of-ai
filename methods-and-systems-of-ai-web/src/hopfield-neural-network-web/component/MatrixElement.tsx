import React, {useEffect, useState} from 'react';
import {Pattern} from "../data/Pattern";

interface Props {
    matrix: Pattern,
    row: number,
    col: number,
    value: number,
    parentWidth: number,
    parentHeight: number,
}

function MatrixElement({matrix, row, col, value, parentWidth}: Props) {
    const [selected, setSelected] = useState(value > 0);
    useEffect(() => {
        setSelected(matrix.p[row][col] > 0);
    }, [matrix]);

    function clickElement(row: number, col: number) {
        matrix.p[row][col] = selected ? 0 : 1;
        setSelected(!selected);
    }

    function dragOver(row: number, col: number) {
        if(selected){
            return;
        }
        clickElement(row, col);
    }

    return <div
        onDragOver={() => dragOver(row, col)}
        onClick={() => clickElement(row, col)}
        style={{width: parentWidth / matrix.p.length, height: parentWidth / matrix.p.length}}
        className={`matrix-element ${selected ? "selected-element" : "unselected-element"}`}>
    </div>
}

export default MatrixElement;