import React from 'react';
import "../styles/default-expert-system.scss"
import RuleScope from "./RuleScope";
import "../styles/block-style.scss"

function ExpertPage() {
    return (
        <main className={"expert-page-main"}>
            <h1>Expert System. Rule creation</h1>
            <RuleScope id={1}/>
        </main>
    );
}

export default ExpertPage;