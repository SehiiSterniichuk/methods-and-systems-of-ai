import React, {useState} from 'react';
import logo from './logo.svg';
import './App.css';

async function sendRequest(v1: number, v2: number) {
    try {
        const url = `http://localhost:8080/api/v1/math/sum/${v1}/${v2}`;

        return await fetch(url, {
            method: 'POST', // or 'POST', 'PUT', etc. depending on your API
        });
    } catch (error) {
        console.error('Error:', error);
    }
}
function App() {
    const [result, setResult] = useState("")
    const [v1, setV1] = useState("")
    const [v2, setV2] = useState("")

    function onInputV1(v: React.FormEvent<HTMLInputElement>) {
        const currentTarget = v.currentTarget.value;
        setV1(currentTarget)
    }

    function onInputV2(v: React.FormEvent<HTMLInputElement>) {
        const currentTarget = v.currentTarget.value;
        setV2(currentTarget)
    }

    function doClick() {
        sendRequest(Number.parseInt(v1), Number.parseInt(v2))
            .then((response) => {
                if (response?.ok) {
                    return response.json();
                } else {
                    throw new Error(`Request failed`);
                }
            })
            .then((data) => {
                setResult(data); // Set the result state with the API response
            })
            .catch((error) => {
                console.error('Error:', error);
                setResult("-1"); // Set the result state to -1 in case of an error
            });
    }


    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>
                    Edit <code>src/App.tsx</code> and save to reload.
                </p>
                <a
                    className="App-link"
                    href="https://reactjs.org"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Learn React
                </a>
                <div className="formStyle">
                    <div className="inputs">
                        <input type="text" id={"input1"} onInput={x => onInputV1(x)}/>
                        <input type="number" id={"input2"} onInput={x => onInputV2(x)}/>
                    </div>
                    <button onClick={() => doClick()}>Calculate!</button>
                </div>
                <p>Result: {result}</p>
            </header>
        </div>
    );
}

export default App;
