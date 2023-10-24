import React from 'react';
import {ActionType, actionTypes} from "../data/ActionType";

interface Props{
    actionType:ActionType
}
function ActionDefinition({actionType}:Props) {
    const titleType = actionType == ActionType.THEN ? "Then" : "Else";
    const title = `${titleType} action definition`;
    return (
        <div className="section action-definition-section">
            <div className="row block-header">
                <p className={"block-title"}>{title}</p>
                <button className={"plus-button"}>+</button>
            </div>
            <div className="input-holder">
                <div className="long-input-wrapper">
                    <div className="input-title-pair long-input">
                        <p>Action</p>
                        <textarea className={"rectangle-input"}/>
                    </div>
                </div>
                <div className="long-input-wrapper">
                    <div className="input-title-pair long-input">
                        <p>Name of new rule</p>
                        <textarea className={"rectangle-input"}/>
                    </div>
                </div>
                <div className="short-input-wrapper">
                    <div className="input-title-pair choice-input">
                        <p>Type</p>
                        <select className={"rectangle-input"}>
                            {actionTypes.map(r => {
                                return <option value={r}>{r.toLowerCase().replace("_", " ")}</option>
                            })}
                        </select>
                    </div>
                    <div className="input-title-pair choice-input">
                        <p>Rule from database</p>
                        <select className={"rectangle-input"}>
                            <option value={"false"}>No</option>
                            <option value={"true"}>Yes</option>
                        </select>
                    </div>
                    <div className="input-title-pair ">
                        <p>Rule ID</p>
                        <input type="number" className={"id-input rectangle-input"} min={1} maxLength={50}/>
                    </div>
                    <div className="input-title-pair ">
                        <p>Formula</p>
                        <input className={"rectangle-input"} type="text"/>
                    </div>
                </div>
            </div>

        </div>
    );
}

export default ActionDefinition;