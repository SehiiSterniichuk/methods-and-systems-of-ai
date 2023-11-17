import React, {useEffect, useRef, useState} from 'react';
import {getMatrix, Pattern} from "./data/Pattern";
import MatrixElement from "./component/MatrixElement";
import "./styles/hopfield.scss";
import {getPattern, postPatterns} from "./networking/HopfieldRequest";

enum HopfieldMode {
    CREATE, FIND
}

function HopfieldApp() {
    const [dimension, setDimension] = useState(5);
    const [patterns, setPatterns] = useState<Pattern[]>([]);
    const [matrix, setMatrix] = useState<Pattern>(getMatrix(dimension));
    const matrixBodyRef = useRef<HTMLDivElement>(null); // Reference for matrix-body dimensions
    const defaultSize = 400;
    const [width, setWidth] = useState(defaultSize)
    const [height, setHeight] = useState(defaultSize)
    const [mode, setMode] = useState(HopfieldMode.CREATE);
    const [name, setName] = useState("")
    const MAX_NUMBER = Math.floor(0.15 * (dimension * dimension));

    function handleAdd() {
        if(MAX_NUMBER <= patterns.length){
            setMessage(`Too many patterns for this network. ${MAX_NUMBER} is maximum`)
            setMatrix(getMatrix(dimension));
            return;
        }
        setPatterns(p => [...p, matrix])
        setMatrix(getMatrix(dimension));
    }

    useEffect(() => {
        if(patterns.length > 0){
            setPatterns([]);
        }
    }, [mode]);

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
        if (dimension != matrix.p.length) {
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

    function sendPatterns() {
        if (patterns.length < 2) {
            setMessage("Add more patterns");
            return;
        }
        if (name.trim().length === 0) {
            setMessage("Enter name");
            return;
        }
        postPatterns({name: name, patterns: patterns})
            .then(id => {
                setMessage(`Saved network with id: ${id}`)
            })
            .catch(e => {
                setMessage("Failed to post")
                console.error(e);
            })
    }

    const [message, setMessage] = useState("")

    function findPattern() {
        const find = matrix.p.flatMap(a => a.flat()).find(n => n > 0) || -1;
        if (find < 0) {
            setMessage("Please, enter some pattern");
            return;
        }
        if (name.trim().length === 0) {
            setMessage("Enter name");
            return;
        }
        getPattern({networkName: name, pattern: matrix})
            .then(r => {
                setMatrix(r)
                setMessage("Pattern is found")
            })
            .catch(e => {
                setMessage("Failed to post")
                console.error(e);
            })
    }

    useEffect(() => {
        if (message.length > 0) {
            setTimeout(() => {
                setMessage("");
            }, 7000)
        }
    }, [message]);
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
                            patterns {MAX_NUMBER}</p>
                        <p className={"pattern-info-p"}>List size: {patterns.length}</p>
                    </div>}
                <div className="row matrix-manager-wrapper">
                    <div className="matrix-manager matrix-manager-wrapper-child">
                        <div className="matrix-body" ref={matrixBodyRef}>
                            {matrix.p.map((r, row) => {
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
                            <p className={"matrix-info-p"}>{message}</p>
                            <div className="row matrix-button-row">
                                {mode != HopfieldMode.CREATE ? null :
                                    <button onClick={handleAdd} className={"matrix-button"}>Add</button>}
                                <button onClick={() => setMatrix(getMatrix(dimension))}
                                        className={`matrix-button ${mode == HopfieldMode.FIND ? 'full-width' : ''}`}
                                >Clear
                                </button>
                            </div>
                            {mode != HopfieldMode.CREATE ? null :
                                <button onClick={() => sendPatterns()} className={"matrix-button"}>Create</button>}
                            {mode != HopfieldMode.FIND ? null :
                                <button onClick={() => findPattern()} className={"matrix-button"}>Find</button>}
                        </div>
                    </div>
                </div>
            </div>
        </main>
    );
}

export default HopfieldApp;