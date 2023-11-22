import React, {useEffect, useRef, useState} from 'react';
import {getMatrix, Pattern} from "./data/Pattern";
import MatrixElement from "./component/MatrixElement";
import "./styles/hopfield.scss";
import {getPattern, getPatternImg, postPatterns, postPatternsImg} from "./networking/HopfieldRequest";
import default_img from "./img/default_img.png";

enum HopfieldMode {
    CREATE, FIND
}

enum DataMode {
    FILE, MATRIX
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
    const [dataMode, setDataMode] = useState(DataMode.MATRIX)
    const [name, setName] = useState("")
    const MAX_NUMBER = Math.floor(0.15 * (dimension * dimension));
    const [images, setImages] = useState<File[]>([]);

    const handleImageChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (event.target == null || event.target.files == null) {
            return;
        }
        if (event.target.files) {
            if (mode == HopfieldMode.CREATE) {
                const selectedImages: File[] = Array.from(event.target.files);
                setImages(o => [...o, ...selectedImages]);
                return;
            }
            const selectedImages: File[] = Array.from(event.target.files);
            setImages(selectedImages);
        }
    };

    function handleAdd() {
        if (dataMode == DataMode.MATRIX) {
            if (MAX_NUMBER <= patterns.length) {
                setMessage(`Too many patterns for this network. ${MAX_NUMBER} is maximum`)
                setMatrix(getMatrix(dimension));
                return;
            }
            setPatterns(p => [...p, matrix])
            setMatrix(getMatrix(dimension));
            return;
        }
    }

    useEffect(() => {
        if (patterns.length > 0) {
            setPatterns([]);
        }
        if (images.length > 0) {
            setImages([])
        }
    }, [mode]);
    useEffect(() => {
        if (dataMode === DataMode.FILE) {
            setPatterns([]);
            setMatrix(getMatrix(dimension))
        } else {
            setImages([])
        }
    }, [dataMode]);
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
        if (name.trim().length === 0) {
            setMessage("Enter name");
            return;
        }
        let promise: Promise<number>;
        if (dataMode === DataMode.MATRIX) {
            if (patterns.length < 2) {
                setMessage("Add more patterns");
                return;
            }
            promise = postPatterns({name: name, patterns: patterns});
        } else {
            if (images.length < 2) {
                setMessage("Add more patterns");
                return;
            }
            promise = postPatternsImg(images, name);
        }
        promise
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
        if (name.trim().length === 0) {
            setMessage("Enter name");
            return;
        }
        if (dataMode == DataMode.MATRIX) {
            const find = matrix.p.flatMap(a => a.flat()).find(n => n > 0) || -1;
            if (find < 0) {
                setMessage("Please enter some pattern");
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
            return;
        }
        if (images.length < 0) {
            setMessage("Please select an image");
            return;
        }
        getPatternImg(images[0], name)
            .then(i => {
                setMessage("Pattern is found")
                setImages([i])
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
    const matrixData = <div className="matrix-body" ref={matrixBodyRef}>
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
    </div>;
    const fileData = <div className="matrix-body img-input-body">
        {images.length > 0 &&
            <img src={URL.createObjectURL(images[images.length - 1])} className={"img-input"} alt="Selected"/>}
        {images.length === 0 && <img src={default_img} className={"img-input"} alt="Selected"/>}
    </div>;

    function handleClear() {
        if (dataMode == DataMode.MATRIX) {
            setMatrix(getMatrix(dimension))
            return;
        }
        setImages([]);
    }
    return (
        <main>
            <h1 className={"hopfield-h1"}>Hopfield Network</h1>
            <h2 className={"hopfield-h2"}>{mode === HopfieldMode.CREATE ? "Create network" : "Find pattern"}</h2>
            <div className={"hopfield-change-mode-holder"}>
                <button onClick={() => setMode((mode + 1) % 2)}>Change mode</button>
                <button onClick={() => setDataMode((dataMode + 1) % 2)}>Change data type</button>
            </div>
            <div className="matrix-manipulation-body">
                {mode != HopfieldMode.CREATE || dataMode == DataMode.FILE? null :
                    <div className="matrix-manipulation-info">
                        <p className={"pattern-info-p"}>Maximum number of
                            patterns {MAX_NUMBER}</p>
                        <p className={"pattern-info-p"}>List
                            size: {dataMode === DataMode.MATRIX ? patterns.length : images.length}</p>
                    </div>}
                <div className="row matrix-manager-wrapper">
                    <div className="matrix-manager matrix-manager-wrapper-child">
                        {dataMode == DataMode.MATRIX ? matrixData : fileData}
                    </div>
                    <div className="matrix-info matrix-manager-wrapper-child">
                        <div className="row">
                            <div className="matrix-info-label">
                                {dataMode == DataMode.FILE ? null :
                                    <p className={"matrix-info-e matrix-info-p"}>Dimension: </p>}
                                <p className={"matrix-info-e matrix-info-p"}>Pattern name: </p>
                            </div>
                            <div className="matrix-info-input">
                                {dataMode == DataMode.FILE ? null :
                                    <input className={"matrix-info-e matrix-info-dimension"}
                                           onChange={event => handleDimension(event)} defaultValue={dimension}
                                           type="number"/>}
                                <input className={"matrix-info-e"} onChange={e => handleName(e)} type="text"
                                       placeholder={"Enter name..."}/>
                            </div>
                        </div>
                        <div className="matrix-menu">
                            <p className={"matrix-info-p"}>{message}</p>
                            {dataMode != DataMode.FILE || mode == HopfieldMode.FIND ? null :
                                <input type="file" multiple onChange={handleImageChange} accept="image/*"/>}
                            {dataMode != DataMode.FILE || mode == HopfieldMode.CREATE ? null :
                                <input type="file" onChange={handleImageChange} accept="image/*"/>}
                            <div className="row matrix-button-row">
                                {mode != HopfieldMode.CREATE || dataMode == DataMode.FILE ? null :
                                    <button onClick={handleAdd} className={"matrix-button"}>Add</button>}
                                <button onClick={() => handleClear()}
                                        className={`matrix-button ${mode == HopfieldMode.FIND || dataMode == DataMode.FILE ? 'full-width' : ''}`}
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
                {dataMode != DataMode.FILE || mode == HopfieldMode.FIND ? null :
                    <>
                        <h3 className={"saved-h3-list"}>Saved in list:</h3>
                        <div className="selected-img-list">
                            {images.map((p, i) => <img className={"small-preview-selected-img"}
                                                       src={URL.createObjectURL(p)}
                                                       key={`img-item-${i}`} alt="img"/>)}
                        </div>
                    </>
                }
            </div>
        </main>
    );
}

export default HopfieldApp;