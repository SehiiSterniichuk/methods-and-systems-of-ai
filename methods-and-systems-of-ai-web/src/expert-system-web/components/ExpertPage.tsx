import React, {useState} from 'react';
import "../styles/default-expert-system.scss"
import RuleScope from "./RuleScope";
import "../styles/block-style.scss"
import {ActionDTO, returnEmptyActionWithId, returnEmptyRuleWithId, RuleDTO} from "../data/ActionDTO";
import {RuleType} from "../data/ActionType";
import {PostTaskRequest} from "../../travelling-salesman-web/data/TaskData";
import {SERVER_URL} from "../../travelling-salesman-web/data/Constants";
import {sendRules} from "../networking/Requests";

function ExpertPage() {
    const [rules, setRules] = useState([returnEmptyRuleWithId(1)])

    function addNewRule() {
        const newActionId = rules.length + 1;
        const newRuleScopes = [...rules, returnEmptyActionWithId(newActionId)];
        setRules(newRuleScopes);
    }

    const [message, setMessage] = useState("");

    function sendNewRules() {
        if (message.length >= 1) {
            setMessage("");
        }

        function checkGoToAction(ruleDTO: RuleDTO) {
            const isProvidedId = ruleDTO.id === undefined || ruleDTO.id <= 0;
            if (isProvidedId) {
                return -1;
            }
            const isValidName =
                ruleDTO.name === undefined ||
                ruleDTO.name.trim() === "" ||
                rules.find(x => x.name === ruleDTO.name) === undefined;
            if (isValidName) {
                return -1;
            }
            return 0;
        }

        function checkAction(actionList: ActionDTO[]) {
            for (let i = 0; i < actionList.length; i++) {
                let action = actionList[i];
                if ((action.name === undefined || action.name.trim() === "")
                    && (action.gotoAction === undefined || action.gotoAction.length === 0 ||
                        (checkGoToAction(action.gotoAction[0]) !== -1)
                    )
                ) {
                    return i;
                }
            }
            return -1;
        }

        function checkActionPower(actionList: ActionDTO[], r: RuleDTO, actionName: string): number {
            const type = r.decisionInfo?.type || RuleType.BINARY;
            if (type === RuleType.BINARY || type === RuleType.BINARY_FORMULA) {
                return -1;
            }
            for (let i = 0; i < actionList.length; i++) {
                let action = actionList[i];
                if (action.formula === undefined || action.formula.trim() === "") {
                    setMessage(`missed formula for the ${actionName} #${r.id}.${action.id}`)
                    return action.id as number;
                }
            }
            return -1;
        }

        function checkActions(action: ActionDTO[] | undefined, actionName: string, r: RuleDTO) {
            if (action === undefined || action.length === 0) {
                setMessage(`${actionName} undefined for rule: #${r.id}`)
                return 1;
            }
            const aId = checkAction(action);
            if (aId !== -1) {
                setMessage(`${actionName} undefined for rule action: #${r.id}.${aId}`)
                return 2;
            }
            return -1;
        }

        function parseActionArrayForARequest(actions: ActionDTO[] | undefined) {
            if (actions === undefined) {
                return [];
            }

            function isGoToEmpty(gotoAction: RuleDTO[] | undefined) {
                return gotoAction === undefined || gotoAction.length == 0 ||
                    (isEmptyName(gotoAction[0].name) && isEmptyId(gotoAction[0].id));
            }

            function isEmptyName(name: string | undefined) {
                return name === undefined || name.trim() === "";
            }

            function isEmptyId(id: number | undefined) {
                return id === undefined || id <= 0;
            }

            return actions
                .filter(a => !(isGoToEmpty(a.gotoAction) && isEmptyName(a.name)))
                .map(a => ({...a, id: undefined} as ActionDTO))
        }

        function parseArrayForARequest(rules: RuleDTO[]) {
            return rules.map(r => {
                const thenActions: ActionDTO[] = parseActionArrayForARequest(r.thenAction);
                const elseActions: ActionDTO[] = parseActionArrayForARequest(r.elseAction);
                return {...r, id: undefined, elseAction: elseActions, thenAction: thenActions} as RuleDTO;
            })
        }

        for (let i = 0; i < rules.length; i++) {
            const r = rules[i];
            if (r.name === undefined || r.name.trim() === "") {
                setMessage(`missed name for the rule #${r.id}`)
                return;
            }
            if (r.condition === undefined || r.condition.trim() === "") {
                setMessage(`missed question for the rule #${r.id}`)
                return;
            }
            if (r.decisionInfo?.type !== RuleType.BINARY &&
                (r.decisionInfo?.formula === undefined || r.decisionInfo?.formula.trim() === "")) {
                setMessage(`missed formula for the rule #${r.id}`)
                return;
            }
            const action = r.thenAction;
            const actionName = `thenAction`;
            const checkActionPurpose = checkActions(action, actionName, r);
            if (checkActionPurpose !== -1 ||
                checkActionPower(r.thenAction || [], r, actionName) !== -1) {
                break;
            }

            const elseActionName = "elseAction";
            if (r.decisionInfo.type === RuleType.BINARY ||
                r.decisionInfo.type === RuleType.BINARY_FORMULA) {
                const elseIsValid = checkActions(r.elseAction, elseActionName, r);
                if (elseIsValid !== -1) {
                    break;
                }
            } else if (checkActionPower(r.elseAction || [], r, elseActionName) !== -1) {
                break;
            }
            setMessage("preparing request")
            const preparedRules: RuleDTO[] = parseArrayForARequest(rules);
            setMessage("sending...")
            sendRules(preparedRules)
                .then(x => x.json())
                .then(x => x as number[])
                .then(x => {
                    if (x.length > 0) {
                        setMessage(`Saved with id: ${x.join(',')}`)
                    } else {
                        setMessage("Saved nothing")
                    }
                })
                .catch(e => {
                    setMessage("Error")
                    console.error(e);
                })
        }
    }
    //todo hide formula field for binary_formula actions
    const button = <div className="button-send-wrapper row">
        <p>{message}</p>
        <button onClick={sendNewRules} className={"post-new-rules-btn"}>Post new rules!</button>
    </div>;
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