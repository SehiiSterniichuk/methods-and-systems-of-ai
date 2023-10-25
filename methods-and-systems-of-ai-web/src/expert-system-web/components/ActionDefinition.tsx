import React, {useEffect, useState} from 'react';
import {ActionDecisionType, actionDecisionTypes, ActionType, RuleType} from "../data/ActionType";

interface Props {
    actionType: ActionType
    id: number
    scopeId: number
    parentRuleType: RuleType
    addNew: () => void
}

function ActionDefinition({actionType, id, scopeId, parentRuleType, addNew}: Props) {
    const titleType = actionType == ActionType.THEN ? "Then" : "Else";
    const title = `${titleType} action definition`;
    const [actionDecisionType, setActionDecisionType] = useState(actionDecisionTypes[0]);
    const [isFromDatabase, setIsFromDatabase] = useState(false)
    const [name, setName] = useState("");
    const [formula, setFormula] = useState("")

    function selectTypeStrategy(e: React.ChangeEvent<HTMLSelectElement>) {
        if (e.target == null || e.target.value == null) {
            return;
        }
        const filter = actionDecisionTypes.filter(r => r.toString() == e.target.value);
        if (filter.length >= 1) {
            setActionDecisionType(filter[0]);
        }
    }

    function selectDatabaseStrategy(e: React.ChangeEvent<HTMLSelectElement>) {
        if (e.target == null || e.target.value == null) {
            return;
        }

        function setNewState(b: boolean) {
            if (b != isFromDatabase) {
                setIsFromDatabase(b);
            }
        }

        if ("true" === e.target.value) {
            setNewState(true);
        } else {
            setNewState(false);
        }
    }


    return (
        <div key={`action_${actionType}_${id}_from_scope_${scopeId}`}
             className="section action-definition-section">
            <p>Parent: {parentRuleType}</p>
            <div className="row block-header">
                <p className={"block-title"}>{title}</p>
                <button onClick={addNew}
                        className={"plus-button"}>+
                </button>
            </div>
            <div className="input-holder">
                <div
                    className="long-input-wrapper">
                    <div className="input-title-pair long-input">
                        <p>Action</p>
                        <textarea className={"rectangle-input"}/>
                    </div>
                </div>
                {isFromDatabase && actionDecisionType == ActionDecisionType.GOTO ||
                actionDecisionType == ActionDecisionType.RESULT ? null :
                    <div
                        key={`long-input-wrapper_from_action_${actionType}_${id}_from_scope_${scopeId}`}
                        className="long-input-wrapper">
                        <div className="input-title-pair long-input">
                            <p>Name of new rule</p>
                            <textarea className={"rectangle-input"}/>
                        </div>
                    </div>}

                <div
                    key={`short-input-wrapper_from_action_${actionType}_${id}_from_scope_${scopeId}`}
                    className="short-input-wrapper">
                    <div
                        key={`type_input_from_action_${actionType}_${id}_from_scope_${scopeId}`}
                        className="input-title-pair choice-input">
                        <p>Type</p>
                        <select onChange={e => selectTypeStrategy(e)} className={"rectangle-input"}>
                            {actionDecisionTypes.map(r => {
                                return <option
                                    key={`type_input_${r}_from_action_${actionType}_${id}_from_scope_${scopeId}`}
                                    value={r}>{r.toLowerCase().replace("_", " ")}</option>
                            })}
                        </select>
                    </div>
                    {actionDecisionType == ActionDecisionType.RESULT ? null :
                        <div
                            key={`rule_source_input_from_action_${actionType}_${id}_from_scope_${scopeId}`}
                            className="input-title-pair choice-input">
                            <p>Rule from database</p>
                            <select onChange={(e) => selectDatabaseStrategy(e)} className={"rectangle-input"}>
                                <option value={"false"}>No</option>
                                <option value={"true"}>Yes</option>
                            </select>
                        </div>}

                    {actionDecisionType == ActionDecisionType.RESULT || !isFromDatabase ? null :
                        <div
                            key={`rule_id_input_from_action_${actionType}_${id}_from_scope_${scopeId}`}
                            className="input-title-pair ">
                            <p>Rule ID</p>
                            <input type="number" className={"id-input rectangle-input"} min={1} maxLength={50}/>
                        </div>}
                    {parentRuleType == RuleType.BINARY ? null : <div
                        key={`formula_input_from_action_${actionType}_${id}_from_scope_${scopeId}`}
                        className="input-title-pair ">
                        <p>Formula</p>
                        <input className={"rectangle-input"} type="text"/>
                    </div>}

                </div>
            </div>

        </div>
    );
}

export default ActionDefinition;