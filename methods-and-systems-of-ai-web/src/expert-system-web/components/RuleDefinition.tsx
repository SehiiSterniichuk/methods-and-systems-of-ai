import React from 'react';
import {ruleTypes} from "../data/ActionType";

function RuleDefinition() {
    return (
        <div className="section rule-definition-section">
            <div className="row block-header">
                <p className={"block-title"}>Rule definition</p>
                <button className={"plus-button"}>+</button>
            </div>
            <div className="input-holder">
                <div className="long-input-wrapper">
                    <div className="input-title-pair long-input">
                        <p>Name</p>
                        <textarea className={"rectangle-input"}/>
                    </div>
                    <div className="input-title-pair long-input">
                        <p>Question</p>
                        <textarea className={"rectangle-input"}/>
                    </div>
                </div>
                <div className="short-input-wrapper">
                    <div className="input-title-pair choice-input">
                        <p>Type</p>
                        <select className={"rectangle-input"}>
                            {ruleTypes.map(r => {
                                return <option value={r}>{r.toLowerCase().replace("_", " ")}</option>
                            })}
                        </select>
                    </div>
                    <div className="input-title-pair ">
                        <p>Variables</p>
                        <input className={"rectangle-input"} type="text"/>
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

export default RuleDefinition;