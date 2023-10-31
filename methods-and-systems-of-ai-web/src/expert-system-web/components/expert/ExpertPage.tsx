import React, {useState} from 'react';
import "../../styles/default-expert-system.scss"
import RuleScope from "./RuleScope";
import "../../styles/block-style.scss"
import {returnEmptyActionWithId, returnEmptyRuleWithId} from "../../data/ActionDTO";
import PostButton from "./PostButton";

function ExpertPage() {
    const [rules, setRules] = useState([returnEmptyRuleWithId(1)])
    function addNewRule() {
        const newActionId = rules.length + 1;
        const newRuleScopes = [...rules, returnEmptyActionWithId(newActionId)];
        setRules(newRuleScopes);
    }
    const button = <PostButton rules={rules}/>
    return (
        <main className={"expert-page-main"}>
            <h1>Expert System. Rule manipulation</h1>
            {button}
            {rules.map(r => {
                return <RuleScope ruleScopeId={r.id ? r.id : -1} key={`RuleScope_component: ${r.id}`}
                                  addNewRule={addNewRule} ruleObj={r}/>
            })}
            {button}
        </main>
    );
}

export default ExpertPage;