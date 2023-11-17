import React, {useEffect, useRef, useState} from 'react';
import {getMatrix, Matrix} from "./component/Matrix";
import MatrixElement from "./component/MatrixElement";
import "./styles/hopfield.scss";

enum HopfieldMode {
    CREATE, FIND
}

function HopfieldApp() {
    const [dimension, setDimension] = useState(5);
    const [patterns, setPatterns] = useState<Matrix[]>([]);
    const [matrix, setMatrix] = useState<Matrix>(getMatrix(dimension));
    const matrixBodyRef = useRef<HTMLDivElement>(null); // Reference for matrix-body dimensions
    const defaultSize = 400;
    const [width, setWidth] = useState(defaultSize)
    const [height, setHeight] = useState(defaultSize)
    const [mode, setMode] = useState(HopfieldMode.CREATE);
    const [name, setName] = useState("")

    function handleAdd() {
        setPatterns(p => [...p, matrix])
        setMatrix(getMatrix(dimension));
    }

    useEffect(() => {
        // Get the dimensions of matrix-body and pass it to MatrixElement
        if (matrixBodyRef.current) {
            const {clientWidth, clientHeight} = matrixBodyRef.current;
            // Here, you can perform any logic with the width and height obtained
            setHeight(clientHeight);
            setWidth(clientWidth);
            console.log('Width:', clientWidth, 'Height:', clientHeight);
        }
    }, []);
    useEffect(() => {
        if (dimension != matrix.array.length) {
            setMatrix(getMatrix(dimension))
            setPatterns([]);
        }
    }, [dimension]);

    function handleDimension(event: React.ChangeEvent<HTMLInputElement>) {
        if (event.target == null || event.target.value == null) {
            return;
        }
        setDimension(Number.parseInt(event.target.value))
    }

    function handleName(event: React.ChangeEvent<HTMLInputElement>) {
        if (event.target == null || event.target.value == null) {
            return;
        }
        setName(event.target.value)
    }

    console.log(name);
    return (
        <main>
            <h1 className={"hopfield-h1"}>Hopfield Network</h1>
            <h2 className={"hopfield-h2"}>{mode === HopfieldMode.CREATE ? "Create network" : "Find pattern"}</h2>
            <div className={"hopfield-change-mode-holder"}>
                <button onClick={() => setMode((mode + 1) % 2)}>Change mode</button>
            </div>
            <div className="matrix-manipulation-body">
                {mode != HopfieldMode.CREATE ? null :
                    <div className="matrix-manipulation-info">
                        <p className={"pattern-info-p"}>Maximum number of
                            patterns {Math.floor(0.15 * (dimension * dimension))}</p>
                        <p className={"pattern-info-p"}>List size: {patterns.length}</p>
                    </div>}

                <div className="row matrix-manager-wrapper">
                    <div className="matrix-manager matrix-manager-wrapper-child">
                        <div className="matrix-body" ref={matrixBodyRef}>
                            {matrix.array.map((r, row) => {
                                return <div key={`${dimension}-matrix-${patterns.length + 1}-row-${row}`}
                                            className={"matrix-row"}>
                                    {r.map((e, col) => {
                                        return <MatrixElement matrix={matrix}
                                                              value={e}
                                                              parentHeight={height}
                                                              parentWidth={width}
                                                              key={`${dimension}-matrix-${patterns.length + 1}-element-${row}-${col}`}
                                                              col={col} row={row}/>
                                    })}
                                </div>
                            })}
                        </div>

                    </div>
                    <div className="matrix-info matrix-manager-wrapper-child">
                        <div className="row">
                            <div className="matrix-info-label">
                                <p className={"matrix-info-e matrix-info-p"}>Dimension: </p>
                                <p className={"matrix-info-e matrix-info-p"}>Pattern name: </p>
                            </div>
                            <div className="matrix-info-input">
                                <input className={"matrix-info-e matrix-info-dimension"}
                                       onChange={event => handleDimension(event)} defaultValue={dimension}
                                       type="number"/>
                                <input className={"matrix-info-e"} onChange={e => handleName(e)} type="text"
                                       placeholder={"Enter name..."}/>
                            </div>
                        </div>
                        <div className="matrix-menu">
                            <div className="row matrix-button-row">
                                {mode != HopfieldMode.CREATE ? null :
                                    <button onClick={handleAdd} className={"matrix-button"}>Add</button>}
                                <button onClick={() => setMatrix(getMatrix(dimension))}

                                        className={`matrix-button ${mode == HopfieldMode.FIND? 'full-width' : ''}`}>Clear
                                </button>
                            </div>
                            {mode != HopfieldMode.CREATE ? null : <button className={"matrix-button"}>Create</button>}
                            {mode != HopfieldMode.FIND ? null : <button className={"matrix-button"}>Find</button>}
                        </div>
                    </div>
                </div>
            </div>
        </main>
    );
}

export default HopfieldApp;