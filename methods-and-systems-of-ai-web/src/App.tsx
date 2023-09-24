import React, {useState} from 'react';
import './App.css';
import './styles/App.scss';
import {FirstTask} from "./data/TaskData";
import Task from "./components/Task";

// async function sendRequest(v1: number, v2: number) {
//     try {
//         const url = `http://localhost:8080/api/v1/math/sum/${v1}/${v2}`;
//         return await fetch(url, {
//             method: 'POST', // or 'POST', 'PUT', etc. depending on your API
//         });
//     } catch (error) {
//         console.error('Error:', error);
//     }
// }


function App() {
    const [taskData, setTaskData] = useState([FirstTask]);
    const taskList = taskData.map(t=>(<Task taskId={t.taskId}/>));
    return (
        <div className="App App-header salesman-body">
            <h1>Traversal salesman problem</h1>
            <div className="task-wrapper">
                <h2 className={"task-list-title"}>Tasks: </h2>
                <div className="task-list">
                    {taskList}
                </div>
            </div>
        </div>
    );
}

export default App;
