import React, {useEffect, useRef, useState} from 'react';
import {RuleDTO} from "../../data/ActionDTO";
import "../../styles/chat.scss"
import {getRuleById} from "../../networking/Requests";
import {getOutputVariable, makeDecision} from "../../networking/DecisionMaking";
import {Decision, DecisionRequest, DecisionResponse, DecisionStatus, Variable} from "../../data/Decision";
import {RuleType} from "../../data/ActionType";
import SurveyMessage from "./SurveyMessage";

interface Props {
    startChatId: number
}

export enum MessageType {
    USER, EXPERT
}

export type Message = {
    type: MessageType
    mId: number
    simpleMessage?: string
    rule?: RuleDTO
    decisions?: Decision[]
}
export type VariableWrapper = {
    pointerInArray: number
    variableName: string
}

function ChatPage({startChatId}: Props) {
    const firstMessage = {mId: 0, type: MessageType.EXPERT, simpleMessage: "Hello!"} as Message;
    const [messages, setMessages] = useState<Message[]>([firstMessage]);
    const [inputValue, setInputValue] = useState("");
    const [outputVariable, setOutputVariable] = useState("");
    const chatContainerRef = useRef<HTMLLIElement>(null);
    const [lastExpertQuestion, setLastExpertQuestion] = useState<RuleDTO>({})
    const [variablePointer, setVariablePointer] = useState<VariableWrapper>({
        pointerInArray: 0,
        variableName: ""
    })
    const [variables, setVariables] = useState<Variable[]>([])
    const [isChatEnded, setChatEnd] = useState(false);

    function addNewExpertMessage(x: RuleDTO) {
        const newMessage: Message = {mId: messages.length + 1, type: MessageType.EXPERT, rule: x}
        setLastExpertQuestion(x);
        let getVarMessage: Message | undefined = undefined;
        if (x.decisionInfo) {
            if (x.decisionInfo.type === RuleType.BINARY) {
                setVariablePointer({pointerInArray: 0, variableName: outputVariable})
            } else if (x.decisionInfo.variables.length > 0) {
                const varName = x.decisionInfo.variables[0];
                setVariablePointer({pointerInArray: 0, variableName: varName})
                getVarMessage = {
                    mId: messages.length + 2,
                    simpleMessage: convertToVarMessage(varName),
                    type: MessageType.EXPERT
                }
            }
        }
        if (getVarMessage === undefined) {
            setMessages([...messages, newMessage]);
        } else {
            setMessages([...messages, newMessage, getVarMessage]);
        }
    }

    function convertToVarMessage(variableName: string) {
        return `Enter value of the ${variableName}:`
    }

    function sendSimpleMessage(s: string, type: MessageType) {
        const newMessage: Message = {mId: messages.length + 1, type: type, simpleMessage: s}
        setMessages([...messages, newMessage]);
    }

    function fetchAndSetNewRule(id: number) {
        getRuleById(id)
            .then(x => x.json())
            .then(x => x as RuleDTO)
            .then(x => addNewExpertMessage(x))
            .catch(e => {
                sendSimpleMessage("Failed to fetch rule with id: " + id, MessageType.EXPERT);
                console.error(e);
            })
    }

    useEffect(() => {
        if (startChatId >= 0) {
            fetchAndSetNewRule(startChatId);
        }
        if (outputVariable.length > 0) {
            return;
        }
        getOutputVariable()
            .then(x => x.text())
            .then(x => {
                if (x.trim().length === 0) {
                    throw new Error("Invalid ouput variable")
                }
                setOutputVariable(x)
            })
            .catch(e => {
                sendSimpleMessage("Failed to fetch output variable from the server", MessageType.EXPERT)
                console.error(e);
            })
    }, []);

    function scrollToBottom() {
        if (chatContainerRef.current) {
            const lastChildElement = chatContainerRef.current?.lastElementChild;
            lastChildElement?.scrollIntoView({behavior: 'smooth'});
        }
    }

    useEffect(() => {
        scrollToBottom()
    }, [messages]);


    function handleDecisionResponse(response: DecisionResponse) {
        function handleSingleDecision(decision: Decision) {
            if (decision.action.gotoAction !== undefined && decision.action.gotoAction.length > 0) {
                const id = decision.action.gotoAction[0].id;
                if (id === undefined) {
                    sendSimpleMessage("Server didn't provide id to GOTO rule :(", MessageType.EXPERT)
                    return;
                }
                fetchAndSetNewRule(id)
            }
            if (decision.action.name === undefined) {
                sendSimpleMessage("Server provided empty action:( :(", MessageType.EXPERT)
                return;
            }
            sendSimpleMessage(decision.action.name, MessageType.EXPERT);
            setChatEnd(true);
        }

        function handleMultipleDecisions(decisions: Decision[]) {
            const newDecisionMessage: Message = {
                mId: messages.length + 1,
                type: MessageType.EXPERT,
                decisions: decisions
            };
            setMessages([...messages, newDecisionMessage])
        }

        let decisions: Decision[] = response.decisions.filter(x => x.status === DecisionStatus.SUCCESS)
        if (decisions.length === 0) {
            sendSimpleMessage("Server has given no decision :(", MessageType.EXPERT)
        } else if (decisions.length === 1) {
            handleSingleDecision(decisions[0])
        } else {
            handleMultipleDecisions(decisions);
        }
    }

    function handleUserResponse(userInput: string) {
        const newVariable: Variable = {value: userInput, name: variablePointer.variableName}
        if (lastExpertQuestion.decisionInfo === undefined) {
            return;
        }
        if (lastExpertQuestion.decisionInfo.type != RuleType.BINARY &&
            variablePointer.pointerInArray < lastExpertQuestion.decisionInfo?.variables.length - 1) {
            const newPointer = variablePointer.pointerInArray + 1;
            const newVarName = lastExpertQuestion.decisionInfo.variables[newPointer];
            setVariablePointer({
                pointerInArray: newPointer,
                variableName: newVarName
            })
            setVariables([...variables, newVariable])
            const getVarMessage: Message = {
                mId: messages.length + 1,
                simpleMessage: convertToVarMessage(newVarName),
                type: MessageType.EXPERT
            }
            setMessages([...messages, getVarMessage])
            return;
        }
        if (lastExpertQuestion.id !== undefined && lastExpertQuestion.id >= 0) {
            const request: DecisionRequest = {variables: [...variables, newVariable], ruleId: lastExpertQuestion.id}
            makeDecision(request)
                .then(response => {
                    handleDecisionResponse(response);
                })
                .catch(e => {
                    sendSimpleMessage(`Failed to get decision from rule id: ${lastExpertQuestion.id}`, MessageType.EXPERT);
                    console.error(e);
                })
        }
    }

    function userSendMessage(value: string) {
        const userInput = value.replace(/\s+/g, ' ').trim();
        if (userInput.length > 0) {
            sendSimpleMessage(userInput, MessageType.USER);
        }
    }

    useEffect(() => {
        const message = messages[messages.length - 1];
        if (message.type === MessageType.USER) {
            handleUserResponse(message.simpleMessage || "");
            setInputValue("");
        }
    }, [messages]);

    function handleChange(e: React.ChangeEvent<HTMLTextAreaElement>) {
        if (e.currentTarget == null || e.currentTarget.value == null || e.currentTarget.value.length === 1) {
            return;
        }
        const value = e.currentTarget.value;
        if (value.length >= 2 && value.at(value.length - 1) === "\n") {
            userSendMessage(value);
            console.log("hello")
            e.currentTarget.value = "";
            return;
        }
        const newValue = e.currentTarget.value.replace(/\s+/g, ' ');
        e.currentTarget.value = newValue;
        setInputValue(newValue)
    }

    function getMessageItem(m: Message) {
        function textMessage() {
            return <>
                <p className={"simple-message"}>{m.simpleMessage}</p>
                <div className="rule-data">
                    <p className={"rule-name"}>{m.rule?.name}</p>
                    <p className={"rule-condition"}>{m.rule?.condition}</p>
                </div>
            </>;
        }

        return <div
            className={"message " + (m.type === MessageType.USER ? "user-message" : "expert-message")}>
            {m.decisions === undefined ? textMessage() :
                <SurveyMessage
                    fetchAndSetNewRule={fetchAndSetNewRule}
                    sendSimpleMessage={sendSimpleMessage}
                    m={m}
                />}
        </div>;
    }

    return (
        <section className={"chat-section"}>
            <h1>Expert System</h1>
            <div className="chat-wrapper">
                <ol className={"chat-container"}>
                    {messages.map(m =>
                        <li
                            ref={chatContainerRef}
                            key={`message_from_${m.type}_id_${m.mId}`}
                            className={"message-wrapper " + (m.type === MessageType.USER ? "user-message-wrapper" : "expert-message-wrapper")}>
                            {getMessageItem(m)}
                        </li>)}
                </ol>
                <div className="user-input-wrapper">
                    <div className="input-handler-wrapper">
                        <textarea disabled={isChatEnded} id={"user-input-message"} onChange={e => handleChange(e)}/>
                    </div>
                    <button id={"send-button"} disabled={isChatEnded} onClick={() => userSendMessage(inputValue)}>Send
                    </button>
                </div>
            </div>
        </section>
    );
}

export default ChatPage;