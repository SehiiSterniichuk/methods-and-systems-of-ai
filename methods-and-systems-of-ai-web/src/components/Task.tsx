import React from 'react';
import TaskConfiguration from "./TaskConfiguration";
import PathView from "./PathView";

interface Props {
    taskId:number;
}

function Task({taskId}:Props) {
    return (
        <div className={"task"}>
            <TaskConfiguration taskConfigId={taskId}/>
            <PathView/>
        </div>
    );
}

export default Task;