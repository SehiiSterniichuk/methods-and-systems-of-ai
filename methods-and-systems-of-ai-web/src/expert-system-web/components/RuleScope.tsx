import React from 'react';
import RuleDefinition from "./RuleDefinition";
import ActionDefinition from "./ActionDefinition";
import ActionType from "../data/ActionType";
import "../styles/block-style.scss"
function RuleScope() {
    return (
        <section className={"rule-scope"}>
            <RuleDefinition/>
            <ActionDefinition actionType={ActionType.THEN}/>
            <ActionDefinition actionType={ActionType.ELSE}/>
        </section>
    );
}

export default RuleScope;