import React from 'react';
import "./styles/default-expert-system.scss"
function ExpertSystemApp() {
    return (
        <div className={"expert-system-mode-wrapper"}>
            <h1>Expert System</h1>
            <button>Expert mode</button>
            <button>User mode</button>
        </div>
    );
}

export default ExpertSystemApp;