import React, {useState} from 'react';
import './App.css';
import './styles/App.scss';
import {FirstTask} from "./data/TaskData";
import Task from "./components/Task";

function App() {
    const [taskData, setTaskData] = useState([FirstTask]);
    const taskList = taskData.map(t=>(<Task webTaskId={t.taskId}/>));
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
