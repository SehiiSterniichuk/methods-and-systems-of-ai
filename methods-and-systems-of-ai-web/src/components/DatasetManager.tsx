import React, {useState} from 'react';
import saveImg from "../img/save-button.png"
import downloadImg from "../img/download.svg"
import successIcon from "../img/success-icon.svg"
import failedIcon from "../img/failed-icon.svg"
import updateIcon from "../img/update-icon.svg"
import "../styles/DatasetSaver.scss"
import {Point} from "../data/TaskData";
import {SERVER_URL} from "../data/Constants";
import {Dataset} from "../data/Dataset";
import DatasetForm from "./DatasetForm";

interface Props {
    points: Point[];
    setPoints: (value: (((prevState: Point[]) => Point[]) | Point[])) => void
}

async function fetchList() {
    try {
        const response: Response = await fetch(`${SERVER_URL}/api/v1/lab1/data/all`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });
        if (!response.ok) {
            console.error(`HTTP error! Status: ${response.status}`);
        }
        return response;
    } catch (error) {
        throw error;
    }
}

async function fetchPoints(name: string) {
    try {
        const response: Response = await fetch(`${SERVER_URL}/api/v1/lab1/data/${name}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });
        if (!response.ok) {
            console.error(`HTTP error! Status: ${response.status}`);
        }
        return response;
    } catch (error) {
        throw error;
    }
}

enum FetchAllStatus {
    FAILED, SUCCESS, EMPTY, NONE
}

function DatasetManager({points, setPoints}: Props) {
    const [isOpenSaver, setOpenSaver] = useState(false);
    const [saverDatasetName, setSaverDatasetName] = useState("");
    const [saverStatusImage, setSaverStatusImage] = useState("");
    const [isOpenLoader, setOpenLoader] = useState(false);
    const [loaderDatasetName, setLoaderDatasetName] = useState("");
    const [loaderStatusImage, setLoaderStatusImage] = useState("");
    const [savedDatasetsList, setSavedDatasetsList] = useState<string[]>([])
    const [fetchStatus, setFetchStatus] = useState(FetchAllStatus.NONE);

    function getSuggestionButton() {
        function getOpen() {
            if (points.length < 4) {
                return;
            }
            setOpenSaver(true);
        }

        return <div className="save-points-form">
            <p>Click to save dataset: </p>
            <button className={"suggestion-button"} onClick={() => getOpen()}><img src={saveImg}
                                                                                   alt="save dataset open form"/>
            </button>
        </div>
    }

    function getDatasetForm() {
        return <DatasetForm img={saveImg} datasetName={saverDatasetName} onClick={saveClick}
                            statusImage={saverStatusImage}
                            setOpen={setOpenSaver}
                            setStatusImage={setSaverStatusImage}
                            setDatasetName={setSaverDatasetName}/>
    }

    function saveClick() {
        if (saverDatasetName.length < 2 || points.length < 4) {
            return;
        }
        setSaverStatusImage(updateIcon);

        async function submitDataset(dataset: Dataset) {
            try {
                const response = await fetch(`${SERVER_URL}/api/v1/lab1/data`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(dataset),
                });
                if (!response.ok) {
                    console.error(`HTTP error! Status: ${response.status}`);
                }
                return response;
            } catch (error) {
                throw error;
            }
        }

        submitDataset({name: saverDatasetName, points: points})
            .then(() => setSaverStatusImage(successIcon))
            .catch((e) => {
                console.error(e)
                setSaverStatusImage(failedIcon);
            })
    }

    function getSuggestionLoaderButton() {
        function setFailedFetchStatus() {
            setFetchStatus(FetchAllStatus.FAILED);
        }

        if (fetchStatus == FetchAllStatus.NONE) {
            fetchList()
                .then(x => x.json())
                .then(x => x as string[])
                .then(x => {
                    if (x.length == 0) {
                        setFetchStatus(FetchAllStatus.EMPTY)
                        return x;
                    }
                    setSavedDatasetsList(x);
                    setFetchStatus(FetchAllStatus.SUCCESS)
                    return x;
                })
                .catch(setFailedFetchStatus)
        }
        return <div className="save-points-form">
            <p>Click to load dataset: </p>
            <button className={"suggestion-button"} onClick={() => setOpenLoader(true)}><img src={downloadImg}
                                                                                             alt="load dataset opening form"/>
            </button>
        </div>;
    }

    function getLoaderForm() {
        function loadClick() {
            fetchPoints(loaderDatasetName)
                .then(x => x.json())
                .then(x => x as Dataset)
                .then(x => {
                    setPoints(x.points)
                })
                .catch((e) => {
                    console.error(e)
                    setLoaderStatusImage(failedIcon);
                })
        }

        function getDatabaseStatus() {
            let message: string;
            if (fetchStatus === FetchAllStatus.NONE) {
                message = "Reopen"
            } else if (fetchStatus === FetchAllStatus.EMPTY) {
                message = "Database is empty"
            } else if (fetchStatus === FetchAllStatus.SUCCESS) {
                message = `Available datasets: ${savedDatasetsList.length}`
            } else if (fetchStatus === FetchAllStatus.FAILED) {
                message = "Failed to fetch list from the server. Update page"
            } else {
                message = "unexpected case";
            }
            return message;
        }

        return <div>
            <p>{getDatabaseStatus()}</p>
            <DatasetForm img={downloadImg} datasetName={loaderDatasetName} onClick={loadClick}
                         statusImage={loaderStatusImage}
                         setOpen={setOpenLoader}
                         setStatusImage={setLoaderStatusImage}
                         setDatasetName={setLoaderDatasetName}/>
        </div>
    }

    return (
        <div className={"dataset-saver"}>
            {!isOpenSaver ? getSuggestionButton() : getDatasetForm()}
            {!isOpenLoader ? getSuggestionLoaderButton() : getLoaderForm()}
        </div>
    );
}

export default DatasetManager;