import React, {useState} from 'react';
import searchLogo from "../../img/search_logo_user.svg"
import {Question} from "../../data/Question";
import "../../styles/user.scss"
import {findQuestions} from "../../networking/Requests";

interface Props {
    setRuleId: (id: number) => void
}

function SearchPage({setRuleId}: Props) {
    const [questions, setQuestions] = useState<Question[]>([]);

    function handleEnter(e: React.ChangeEvent<HTMLTextAreaElement>) {
        if (e.currentTarget == null || e.currentTarget.value == null) {
            return;
        }
        const value = e.currentTarget.value;
        if (value.length >= 2 && value.at(value.length - 1) === "\n") {
            findQuestions(value)
                .then(x => x.json())
                .then(x => x as Question[])
                .then(x => {
                    if (x.length === 0) {
                        setQuestions([{id: -1, name: "Found nothing", condition: ""}])
                    } else {
                        setQuestions(x);
                    }
                })
                .catch(e => {
                    setQuestions([{id: -1, name: `Failed to fetch`, condition: ""}])
                    console.error(e);
                })
        }
        e.currentTarget.value = e.currentTarget.value.replace(/\s+/g, ' ');
    }

    function handleQuestion(id: number) {
        if (id >= 0) {
            setRuleId(id);
        }
    }

    return (
        <section className={"search-section"}>
            <h1>Expert System</h1>
            <div className="search-container">
                <div className="search-bar row">
                    <img src={searchLogo} alt="search"/>
                    <textarea onChange={e => handleEnter(e)} placeholder="Write your question here and hit enter..."
                              className={"search-input"}/>
                </div>
                <ul className={"search-results"}>
                    {questions.map(q => {
                        return <li key={`question-item ${q.id}`} onClick={() => handleQuestion(q.id)}
                                   className={"question-result"}>
                            <div className="question-text-data">
                                <span className={"question-name"}>{q.name}</span>
                                <span className={"question-condition"}>{q.condition}</span>
                            </div>
                            <p className={"question-id"}>id: {q.id}</p>
                        </li>
                    })}
                </ul>
            </div>
        </section>
    );
}

export default SearchPage;