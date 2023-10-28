import React, {useEffect, useRef, useState} from 'react';
import {RuleDTO} from "../../data/ActionDTO";
import "../../styles/chat.scss"
import {getRuleById} from "../../networking/Requests";

interface Props {
    startChatId: number
}

enum MessageType {
    USER, EXPERT
}

type Message = {
    type: MessageType
    mId: number
    simpleMessage?: string
    rule?: RuleDTO
}

function ChatPage({startChatId}: Props) {
    const firstMessage = {mId: 0, type: MessageType.EXPERT, simpleMessage: "Hello!"} as Message;
    const [messages, setMessages] = useState<Message[]>([firstMessage]);
    const [inputValue, setInputValue] = useState("");
    const chatContainerRef = useRef<HTMLLIElement>(null);

    function addNewExpertMessage(x: RuleDTO) {
        const newMessage: Message = {mId: messages.length + 1, type: MessageType.EXPERT, rule: x}
        setMessages([...messages, newMessage]);
    }

    function addSimpleMessage(s: string, type: MessageType) {
        const newMessage: Message = {mId: messages.length + 1, type: type, simpleMessage: s}
        setMessages([...messages, newMessage]);
    }

    useEffect(() => {
        if (startChatId >= 0) {
            getRuleById(startChatId)
                .then(x => x.json())
                .then(x => x as RuleDTO)
                .then(x => addNewExpertMessage(x))
                .catch(e => {
                    addSimpleMessage("Failed to fetch rule with id: " + startChatId, MessageType.EXPERT);
                    console.error(e);
                })
        }
    }, []);

    function scrollToBottom() {
        if (chatContainerRef.current) {
            const lastChildElement = chatContainerRef.current?.lastElementChild;
            lastChildElement?.scrollIntoView({ behavior: 'smooth' });
        }
    }

    useEffect(() => {
        scrollToBottom()
    }, [messages]);

    function userSendMessage(value: string) {
        addSimpleMessage(value.replace(/\s+/g, ' ').trim(), MessageType.USER);
        setInputValue("");
    }

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
                            <div
                                className={"message " + (m.type === MessageType.USER ? "user-message" : "expert-message")}>
                                <p className={"simple-message"}>{m.simpleMessage}</p>
                                <div className="rule-data">
                                    <p className={"rule-name"}>{m.rule?.name}</p>
                                    <p className={"rule-condition"}>{m.rule?.condition}</p>
                                </div>
                            </div>

                        </li>)}
                </ol>
                <div className="user-input-wrapper">
                    <div className="input-handler-wrapper">
                        <textarea id={"user-input-message"} onChange={e => handleChange(e)}/>
                    </div>
                    <button id={"send-button"} onClick={() => userSendMessage(inputValue)}>Send</button>
                </div>
            </div>
        </section>
    );
}

export default ChatPage;