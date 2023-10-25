import React, {useState} from 'react';
import RuleDefinition from './RuleDefinition';
import ActionDefinition from './ActionDefinition';
import actionType, {ActionType, RuleType} from '../data/ActionType';
import '../styles/block-style.scss';

interface Props {
    id: number;
}

function RuleScope({id}: Props) {
    const [ruleType, setRuleType] = useState(RuleType.BINARY);
    const rule = RuleDefinition({id: 1, ruleType: ruleType, setRuleType: setRuleType, scopeId: id});
    const [thenActions, setThenActions] = useState([1]); // Store the list of action IDs
    const [elseActions, setElseActions] = useState([1]); // Store the list of action IDs

    const addNewAction = (t: ActionType) => {
        if(ruleType === RuleType.BINARY){
            return;
        }
        if (actionType.THEN === t) {
            const newActionId = thenActions.length + 1;
            const numbers = [...thenActions, newActionId];
            setThenActions(numbers);
        } else {
            const newActionId = elseActions.length + 1;
            const numbers = [...elseActions, newActionId];
            setElseActions(numbers);
        }
    };

    return (
        <section key={`rule_scope_${id}`} className={'rule-scope'}>
            {rule.render}
            {thenActions.map(x => {
                return <ActionDefinition key={"ActionDefinition " + x} id={x} scopeId={id} actionType={ActionType.THEN} parentRuleType={ruleType}
                                         addNew={() => addNewAction(ActionType.THEN)}/>
            })}
            {thenActions.map(x => {
                return <ActionDefinition key={"ActionDefinition " + x} id={x} scopeId={id} actionType={ActionType.ELSE} parentRuleType={ruleType}
                                         addNew={() => addNewAction(ActionType.ELSE)}/>
            })}
        </section>
    );
}

export default RuleScope;
