import React, {useEffect, useState} from 'react';
import {ActionDecisionType, actionDecisionTypes, ActionType, RuleType} from "../../data/ActionType";
import ActionDTO, {returnEmptyRuleOnlyWithId, returnEmptyRuleOnlyWithName} from "../../data/ActionDTO";

interface Props {
    actionType: ActionType
    id: number
    scopeId: number
    parentRuleType: RuleType
    addNew: () => void
    actionObj: ActionDTO
}

function ActionDefinition({actionType, id, scopeId, parentRuleType, addNew, actionObj}: Props) {
    const titleType = actionType == ActionType.THEN ? "Then" : "Else";
    const title = `${titleType} action #${scopeId}.${id}`;
    const [actionDecisionType, setActionDecisionType] = useState(actionDecisionTypes[0]);
    const [isFromDatabase, setIsFromDatabase] = useState(false)
    useEffect(() => {
        if(actionDecisionType === ActionDecisionType.RESULT){
            actionObj.gotoAction = undefined;
        }
    }, [actionDecisionType]);
    useEffect(() => {
        if(actionObj.gotoAction && actionObj.gotoAction.length > 0){
            if(isFromDatabase){
                actionObj.gotoAction[0].id = undefined;
            }else {
                actionObj.gotoAction[0].name = undefined;
            }
        }
    }, [isFromDatabase]);
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

    function setStringHandler(e: React.FormEvent<any>,
                              callback: (a: string) => void) {
        if (e.currentTarget == null || e.currentTarget.value == null) {
            return;
        }
        const newName = e.currentTarget.value;
        const updatedName = newName.replace(/\s+/g, ' ');
        callback(updatedName);
        e.currentTarget.value = updatedName;
    }

    function setNameOfNewRule(s: string) {
        if(isFromDatabase){
            return;
        }
        if (actionObj.gotoAction && actionObj.gotoAction.length > 0) {
            actionObj.gotoAction[0].id = undefined;
            actionObj.gotoAction[0].name = s;
            return;
        }
        const rule = returnEmptyRuleOnlyWithName(s);
        actionObj.gotoAction = [rule]
        console.log(actionObj);
    }

    function setGotoId(s: string) {
        if(!isFromDatabase){
            return;
        }
        const newActionId: number = Number.parseInt(s);
        if (actionObj.gotoAction && actionObj.gotoAction.length > 0) {
            actionObj.gotoAction[0].id = newActionId;
            actionObj.gotoAction[0].name = undefined;
            return;
        }
        const rule = returnEmptyRuleOnlyWithId(newActionId);
        actionObj.gotoAction = [rule]
        console.log(actionObj);
    }
    return (
        <div key={`action_${actionType}_${id}_from_scope_${scopeId}`}
             className="section action-definition-section">
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
                        <textarea onChange={(e) => setStringHandler(e, (s)=>actionObj.name = s)} className={"rectangle-input"}/>
                    </div>
                </div>
                {isFromDatabase && actionDecisionType == ActionDecisionType.GOTO ||
                actionDecisionType == ActionDecisionType.RESULT ? null :
                    <div
                        key={`long-input-wrapper_from_action_${actionType}_${id}_from_scope_${scopeId}`}
                        className="long-input-wrapper">
                        <div className="input-title-pair long-input">
                            <p>Name of new rule</p>
                            <textarea onChange={(e) => setStringHandler(e, setNameOfNewRule)} className={"rectangle-input"}/>
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
                            <input type="number"
                                   onChange={e=>setStringHandler(e, s=>setGotoId(s))}
                                   className={"id-input rectangle-input"} min={1} maxLength={50}/>
                        </div>}
                    {parentRuleType == RuleType.BINARY || parentRuleType == RuleType.BINARY_FORMULA ? null : <div
                        key={`formula_input_from_action_${actionType}_${id}_from_scope_${scopeId}`}
                        className="input-title-pair ">
                        <p>Formula</p>
                        <input
                            onChange={(e) => setStringHandler(e, (s)=>actionObj.formula = s)}
                            className={"rectangle-input"} type="text"/>
                    </div>}

                </div>
            </div>

        </div>
    );
}

export default ActionDefinition;