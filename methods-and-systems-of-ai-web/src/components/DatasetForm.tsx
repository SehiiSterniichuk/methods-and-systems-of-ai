import React from 'react';
import closeImg from "../img/close-button.svg";

interface Props {
    setOpen: (value: (((prevState: boolean) => boolean) | boolean)) => void
    onClick: () => void
    statusImage: string
    setStatusImage: (value: (((prevState: string) => string) | string)) => void
    datasetName: string
    setDatasetName: (value: (((prevState: string) => string) | string)) => void
    img: string
}

function DatasetForm({setOpen, img, onClick, setDatasetName, datasetName, setStatusImage, statusImage}: Props) {
    function writeDatasetName(e: React.FormEvent<HTMLInputElement>) {
        if (e.currentTarget == null || e.currentTarget.value == null) {
            return
        }
        const value = e.currentTarget.value;
        setDatasetName(value)
        if (statusImage.length > 0) {
            setStatusImage("");
        }
    }

    function closeSaverForm() {
        setOpen(false)
    }

    return <div className="save-points-form">
        <p>Save dataset with name: </p>
        <input type="text" defaultValue={datasetName} onInput={e => writeDatasetName(e)}/>
        <button className={"suggestion-button"} onClick={onClick}><img src={img}
                                                                       alt="submit form of saving dataset"/>
        </button>
        <button className={"suggestion-button"} onClick={closeSaverForm}><img src={closeImg}
                                                                              alt="close form of saving dataset"/>
        </button>
        {statusImage.length == 0 ? null : <img className={"suggestion-button"} src={statusImage} alt="current status"/>}
    </div>;
}

export default DatasetForm;