import React, {useEffect, useState} from 'react';
import RuleDefinition from './RuleDefinition';
import ActionDefinition from './ActionDefinition';
import actionType, {ActionType, RuleType} from '../data/ActionType';
import '../styles/block-style.scss';
import {returnEmptyActionWithId, RuleDTO} from "../data/ActionDTO";

interface Props {
    ruleScopeId: number;
    ruleObj: RuleDTO
    addNewRule: ()=>void
}

function RuleScope({ruleScopeId, ruleObj, addNewRule}: Props) {
    const [ruleType, setRuleType] = useState(RuleType.BINARY);
    const [thenActions, setThenActions] = useState([returnEmptyActionWithId(1)]); // Store the list of action IDs
    const [elseActions, setElseActions] = useState([returnEmptyActionWithId(1)]); // Store the list of action IDs
    useEffect(() => {
        ruleObj.thenAction = thenActions;
    }, [thenActions]);
    useEffect(() => {
        ruleObj.elseAction = elseActions;
    }, [elseActions]);
    const addNewAction = (t: ActionType) => {
        if (ruleType === RuleType.BINARY) {
            return;
        }
        if (actionType.THEN === t) {
            const newActionId = thenActions.length + 1;
            const numbers = [...thenActions, returnEmptyActionWithId(newActionId)];
            setThenActions(numbers);
        } else {
            const newActionId = elseActions.length + 1;
            const numbers = [...elseActions, returnEmptyActionWithId(newActionId)];
            setElseActions(numbers);
        }
    };

    return (
        <section key={`rule_scope_section_${ruleScopeId}`} className={'rule-scope'}>
            <RuleDefinition key={`RULE_DEFINITION_COMPONENT_${ruleScopeId}`} ruleObj={ruleObj} id={ruleScopeId}
                            addNewRule={addNewRule}
                            scopeId={ruleScopeId} ruleType={ruleType} setRuleType={setRuleType}/>
            {thenActions.map(x => {
                return <ActionDefinition key={"ActionDefinition " + x}
                                         actionObj={x}
                                         id={x.id ? x.id : -1} scopeId={ruleScopeId} actionType={ActionType.THEN}
                                         parentRuleType={ruleType}
                                         addNew={() => addNewAction(ActionType.THEN)}/>
            })}
            {elseActions.map(x => {
                return <ActionDefinition key={"ActionDefinition " + x}
                                         actionObj={x}
                                         id={x.id ? x.id : -1} scopeId={ruleScopeId} actionType={ActionType.ELSE}
                                         parentRuleType={ruleType}
                                         addNew={() => addNewAction(ActionType.ELSE)}/>
            })}
        </section>
    );
}

export default RuleScope;
