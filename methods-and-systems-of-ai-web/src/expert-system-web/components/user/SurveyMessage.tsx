import React, {useState} from 'react';
import {Message, MessageType} from "./ChatPage";

interface Props {
    m: Message
    fetchAndSetNewRule: (id: number) => void
    setChatEnd: (a: boolean) => void
    sendSimpleMessage: (m: string, t: MessageType) => void
}

function SurveyMessage({m, fetchAndSetNewRule,setChatEnd, sendSimpleMessage}: Props) {
    let i = 0;
    const [disabled, setDisabled] = useState(false);

    function handleChoice(i: number) {
        setDisabled(true);
        console.log(m.decisions)
        console.log(i)
        if (m.decisions === undefined || m.decisions[i].action === undefined) {
            return;
        }
        const action = m.decisions[i].action;
        const ruleDTOS = action.gotoAction;
        if (ruleDTOS !== undefined && ruleDTOS.length > 0 && ruleDTOS[0].id !== undefined) {
            fetchAndSetNewRule(ruleDTOS[0].id)
        } else if (action.name && action.name.length > 0) {
            sendSimpleMessage(action.name, MessageType.EXPERT)
            setChatEnd(true);
        } else {
            sendSimpleMessage("Selected undefined choice", MessageType.EXPERT)
        }
    }

    return (
        <div>
            {m.decisions?.map(d => {
                const id = i;
                i++;
                return <label key={`label_${id}_of_the_m_${m.mId}_type_${m.type}`}>
                    <input type="radio" value={id} disabled={disabled} onClick={() => handleChoice(id)}/>
                    <span> {d.value}</span>
                    <p>{d.action.name}</p>
                    <p>{d.action.formula}</p>
                    <p>{d.action.gotoAction && d.action.gotoAction.length > 0 ? d.action.gotoAction[0].name : null}</p>
                </label>
            })}
        </div>
    );
}

export default SurveyMessage;